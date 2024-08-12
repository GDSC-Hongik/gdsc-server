package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.common.constant.GithubConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.AssignmentHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyHistoryRepository;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.gdschongik.gdsc.domain.study.domain.StudyHistoryValidator;
import com.gdschongik.gdsc.domain.study.dto.request.RepositoryUpdateRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import com.gdschongik.gdsc.infra.client.github.GithubClient;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentStudyHistoryService {

    private final MemberUtil memberUtil;
    private final GithubClient githubClient;
    private final StudyHistoryRepository studyHistoryRepository;
    private final AssignmentHistoryRepository assignmentHistoryRepository;
    private final StudyHistoryValidator studyHistoryValidator;

    @Transactional
    public void updateRepository(Long studyHistoryId, RepositoryUpdateRequest request) throws IOException {
        Member currentMember = memberUtil.getCurrentMember();
        StudyHistory studyHistory = studyHistoryRepository
                .findById(studyHistoryId)
                .orElseThrow(() -> new CustomException(STUDY_HISTORY_NOT_FOUND));
        Study study = studyHistory.getStudy();

        boolean isAnyAssignmentSubmitted =
                assignmentHistoryRepository.existsSubmittedAssignmentByMemberAndStudy(currentMember, study);
        String ownerRepo = getOwnerRepo(request.repositoryLink());
        GHRepository repository = githubClient.getRepository(ownerRepo);
        studyHistoryValidator.validateUpdateRepository(
                isAnyAssignmentSubmitted, String.valueOf(repository.getOwner().getId()), currentMember.getOauthId());

        studyHistory.updateRepositoryLink(request.repositoryLink());
        studyHistoryRepository.save(studyHistory);

        log.info(
                "[StudyHistoryService] 레포지토리 입력: studyHistoryId={}, repositoryLink={}",
                studyHistory.getId(),
                request.repositoryLink());
    }

    private String getOwnerRepo(String repositoryLink) {
        int startIndex = repositoryLink.indexOf(GITHUB_DOMAIN) + GITHUB_DOMAIN.length();
        return repositoryLink.substring(startIndex);
    }
}
