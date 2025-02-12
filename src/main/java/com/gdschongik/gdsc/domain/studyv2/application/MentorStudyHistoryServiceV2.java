package com.gdschongik.gdsc.domain.studyv2.application;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.StudyHistoriesCompletedEvent;
import com.gdschongik.gdsc.domain.study.dto.request.StudyCompleteRequest;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryValidator;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyValidator;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorStudyHistoryServiceV2 {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final MemberUtil memberUtil;
    private final StudyValidator studyValidator;
    private final StudyHistoryValidator studyHistoryValidator;
    private final StudyV2Repository studyV2Repository;
    private final StudyHistoryV2Repository studyHistoryV2Repository;

    @Transactional
    public void completeStudy(StudyCompleteRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyV2 study = studyV2Repository
                .findById(request.studyId())
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));
        List<StudyHistoryV2> studyHistories =
                studyHistoryV2Repository.findAllByStudyIdAndStudentIds(request.studyId(), request.studentIds());

        studyValidator.validateStudyMentor(currentMember, study);
        studyHistoryValidator.validateAppliedToStudy(
                studyHistories.size(), request.studentIds().size());

        studyHistories.forEach(StudyHistoryV2::complete);

        applicationEventPublisher.publishEvent(new StudyHistoriesCompletedEvent(
                studyHistories.stream().map(StudyHistoryV2::getId).toList()));

        log.info(
                "[MentorStudyHistoryServiceV2] 스터디 수료 처리: studyId={}, studentIds={}",
                request.studyId(),
                request.studentIds());
    }

    @Transactional
    public void withdrawStudyCompletion(StudyCompleteRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyV2 study = studyV2Repository
                .findById(request.studyId())
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));
        List<StudyHistoryV2> studyHistories =
                studyHistoryV2Repository.findAllByStudyIdAndStudentIds(request.studyId(), request.studentIds());

        studyValidator.validateStudyMentor(currentMember, study);
        studyHistoryValidator.validateAppliedToStudy(
                studyHistories.size(), request.studentIds().size());

        studyHistories.forEach(StudyHistoryV2::withdrawCompletion);

        studyHistoryV2Repository.saveAll(studyHistories);

        log.info(
                "[MentorStudyHistoryServiceV2] 스터디 수료 철회: studyId={}, studentIds={}",
                request.studyId(),
                request.studentIds());
    }
}
