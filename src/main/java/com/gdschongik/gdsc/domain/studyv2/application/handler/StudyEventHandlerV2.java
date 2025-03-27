package com.gdschongik.gdsc.domain.studyv2.application.handler;

import com.gdschongik.gdsc.domain.studyv2.dao.AssignmentHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.AttendanceV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyApplyCanceledEvent;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyApplyCompletedEvent;
import com.gdschongik.gdsc.global.util.DiscordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class StudyEventHandlerV2 {

    private final DiscordUtil discordUtil;
    private final AttendanceV2Repository attendanceRepository;
    private final AssignmentHistoryV2Repository assignmentHistoryRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleStudyApplyCompletedEvent(StudyApplyCompletedEvent event) {
        log.info(
                "[StudyEventHandlerV2] 수강신청 이벤트 수신: memberDiscordId={}, studyDiscordRoleId={}",
                event.memberDiscordId(),
                event.studyDiscordRoleId());

        discordUtil.addRoleToMemberById(event.studyDiscordRoleId(), event.memberDiscordId());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleStudyApplyCanceledEvent(StudyApplyCanceledEvent event) {
        log.info("[StudyEventHandlerV2] 수강신청 취소 이벤트 수신: memberId={}, studyId={}", event.memberId(), event.studyId());

        attendanceRepository.deleteByStudyIdAndMemberId(event.studyId(), event.memberId());
        assignmentHistoryRepository.deleteByStudyIdAndMemberId(event.studyId(), event.memberId());
        discordUtil.removeRoleFromMemberById(event.studyDiscordRoleId(), event.memberDiscordId());
    }
}
