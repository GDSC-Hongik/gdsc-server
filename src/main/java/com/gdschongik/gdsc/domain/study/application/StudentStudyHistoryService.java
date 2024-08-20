package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.common.constant.GithubConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.AssignmentHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyHistoryRepository;
import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyAssignmentHistoryValidator;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.gdschongik.gdsc.domain.study.domain.StudyHistoryValidator;
import com.gdschongik.gdsc.domain.study.dto.request.RepositoryUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.response.AssignmentHistoryResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import com.gdschongik.gdsc.infra.github.client.GithubClient;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
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
    private final StudyDetailRepository studyDetailRepository;
    private final StudyHistoryRepository studyHistoryRepository;
    private final AssignmentHistoryRepository assignmentHistoryRepository;
    private final StudyHistoryValidator studyHistoryValidator;
    private final StudyAssignmentHistoryValidator studyAssignmentHistoryValidator;

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

    // TODO mentee -> study 변환 작업 필요
    @Transactional(readOnly = true)
    public List<AssignmentHistoryResponse> getAllAssignmentHistories(Long studyId) {
        Member currentMember = memberUtil.getCurrentMember();

        return assignmentHistoryRepository.findAssignmentHistoriesByMenteeAndStudyId(currentMember, studyId).stream()
                .map(AssignmentHistoryResponse::from)
                .toList();
    }

    @Transactional
    public void submitAssignment(Long studyDetailId) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyDetail studyDetail = studyDetailRepository
                .findById(studyDetailId)
                .orElseThrow(() -> new CustomException(STUDY_DETAIL_NOT_FOUND));
        boolean isAppliedToStudy = studyHistoryRepository.existsByMenteeAndStudy(currentMember, studyDetail.getStudy());
        LocalDateTime now = LocalDateTime.now();

        AssignmentHistory assignmentHistory = findOrCreate(currentMember, studyDetail);

        studyAssignmentHistoryValidator.validateSubmitAvailable(isAppliedToStudy, now, studyDetail);

        // TODO: 과제 채점 및 과제이력 업데이트 로직 추가

        assignmentHistoryRepository.save(assignmentHistory);
    }

    private AssignmentHistory findOrCreate(Member currentMember, StudyDetail studyDetail) {
        return assignmentHistoryRepository
                .findByMemberAndStudyDetail(currentMember, studyDetail)
                .orElseGet(() -> AssignmentHistory.create(studyDetail, currentMember));
    }
}
