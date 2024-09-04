package com.gdschongik.gdsc.domain.study.application;

import com.gdschongik.gdsc.domain.study.domain.StudyApplyCanceledEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class StudyEventHandler {

    private final CommonStudyService commonStudyService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleStudyApplyCanceledEvent(StudyApplyCanceledEvent event) {
        log.info("[StudyEventHandler] 스터디 수강신청 취소 이벤트 수신: studyId={}, memberId={}", event.studyId(), event.memberId());
        commonStudyService.deleteAttendance(event.studyId(), event.memberId());
        commonStudyService.deleteAssignmentHistory(event.studyId(), event.memberId());
    }
}
