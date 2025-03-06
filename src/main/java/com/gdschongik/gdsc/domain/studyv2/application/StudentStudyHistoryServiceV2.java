package com.gdschongik.gdsc.domain.studyv2.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static java.util.stream.Collectors.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyAchievementV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyAchievementV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryValidatorV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.dto.request.StudyHistoryRepositoryUpdateRequest;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyHistoryMyResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import com.gdschongik.gdsc.infra.github.client.GithubClient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    private final StudyAchievementV2Repository studyAchievementV2Repository;
    private final StudyHistoryValidatorV2 studyHistoryValidatorV2;

    @Transactional
    public void updateMyRepository(StudyHistoryRepositoryUpdateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyV2 study =
                studyV2Repository.findById(request.studyId()).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        StudyHistoryV2 studyHistory = studyHistoryV2Repository
                .findByStudentAndStudy(currentMember, study)
                .orElseThrow(() -> new CustomException(STUDY_HISTORY_NOT_FOUND));
        String repositoryOwnerId = githubClient.getOwnerId(request.repositoryLink());

        studyHistoryValidatorV2.validateUpdateRepository(repositoryOwnerId, currentMember);

        studyHistory.updateRepositoryLink(request.repositoryLink());
        studyHistoryV2Repository.save(studyHistory);

        log.info(
                "[StudentStudyHistoryServiceV2] 내 레포지토리 입력 완료: studyHistoryId={}, repositoryLink={}",
                studyHistory.getId(),
                request.repositoryLink());
    }

    @Transactional(readOnly = true)
    public List<StudyHistoryMyResponse> getMyStudyHistories() {
        Member currentMember = memberUtil.getCurrentMember();
        List<StudyHistoryV2> studyHistories = studyHistoryV2Repository.findAllByStudent(currentMember);
        List<StudyAchievementV2> studyAchievements = studyAchievementV2Repository.findAllByStudent(currentMember);

        Map<StudyV2, List<StudyAchievementV2>> achievementsByStudy =
                studyAchievements.stream().collect(groupingBy(StudyAchievementV2::getStudy));

        return studyHistories.stream()
                .map(history -> StudyHistoryMyResponse.of(
                        history, history.getStudy(), achievementsByStudy.getOrDefault(history.getStudy(), List.of())))
                .toList();
    }

    @Transactional
    public void applyStudy(Long studyId) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyV2 study = studyV2Repository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));

        List<StudyHistoryV2> studyHistories = studyHistoryV2Repository.findAllByStudent(currentMember);
        LocalDateTime now = LocalDateTime.now();

        studyHistoryValidatorV2.validateApplyStudy(study, studyHistories, now);

        StudyHistoryV2 studyHistory = StudyHistoryV2.create(currentMember, study);
        studyHistoryV2Repository.save(studyHistory);

        log.info("[StudentStudyHistoryServiceV2] 스터디 수강신청: studyHistoryId={}", studyHistory.getId());
    }

    @Transactional
    public void cancelStudyApply(Long studyId) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyV2 study = studyV2Repository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        LocalDateTime now = LocalDateTime.now();

        studyHistoryValidatorV2.validateCancelStudyApply(study, now);

        StudyHistoryV2 studyHistory = studyHistoryV2Repository
                .findByStudentAndStudy(currentMember, study)
                .orElseThrow(() -> new CustomException(STUDY_HISTORY_NOT_FOUND));
        studyHistoryV2Repository.delete(studyHistory);

        log.info(
                "[StudentStudyHistoryServiceV2] 스터디 수강신청 취소: appliedStudyId={}, memberId={}",
                study.getId(),
                currentMember.getId());
    }
}
