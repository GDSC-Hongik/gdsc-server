package com.gdschongik.gdsc.domain.studyv2.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryValidatorV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.dto.request.StudyHistoryRepositoryUpdateRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import com.gdschongik.gdsc.infra.github.client.GithubClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentStudyHistoryServiceV2 {

    private final GithubClient githubClient;
    private final MemberUtil memberUtil;
    private final StudyV2Repository studyV2Repository;
    private final StudyHistoryV2Repository studyHistoryV2Repository;
    private final StudyHistoryValidatorV2 studyHistoryValidatorV2;

    @Transactional
    public void updateRepository(Long studyId, StudyHistoryRepositoryUpdateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyV2 study = studyV2Repository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        StudyHistoryV2 studyHistory = studyHistoryV2Repository
                .findByStudentAndStudy(currentMember, study)
                .orElseThrow(() -> new CustomException(STUDY_HISTORY_NOT_FOUND));
        String repositoryOwnerId = githubClient.getOwnerId(request.repositoryLink());

        studyHistoryValidatorV2.validateUpdateRepository(repositoryOwnerId, currentMember);

        studyHistory.updateRepositoryLink(request.repositoryLink());
        studyHistoryV2Repository.save(studyHistory);

        log.info(
                "[료StudentStudyHistoryServiceV2] 레포지토리 입력 완료: studyHistoryId={}, repositoryLink={}",
                studyHistory.getId(),
                request.repositoryLink());
    }
}
