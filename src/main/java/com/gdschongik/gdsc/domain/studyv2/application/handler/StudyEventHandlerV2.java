package com.gdschongik.gdsc.domain.studyv2.application.handler;

import com.gdschongik.gdsc.domain.studyv2.dao.AttendanceV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyApplyCanceledEvent;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyApplyCompletedEvent;
import com.gdschongik.gdsc.global.util.DiscordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class StudyEventHandlerV2 {

    private final DiscordUtil discordUtil;
    private final AttendanceV2Repository attendanceRepository;
    private final StudyHistoryV2Repository studyHistoryRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleStudyApplyCompletedEvent(StudyApplyCompletedEvent event) {
        log.info(
                "[StudyEventHandlerV2] 수강신청 이벤트 수신: memberDiscordId={}, studyDiscordRoleId={}",
                event.memberDiscordId(),
                event.studyDiscordRoleId());

        Guild guild = discordUtil.getCurrentGuild();
        Member member = discordUtil.getMemberById(event.memberDiscordId());
        Role studyRole = discordUtil.findRoleById(event.studyDiscordRoleId());

        guild.addRoleToMember(member, studyRole).queue();
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleStudyApplyCanceledEvent(StudyApplyCanceledEvent event) {
        log.info("[StudyEventHandlerV2] 수강신청 취소 이벤트 수신: memberId={}, studyId={}", event.memberId(), event.studyId());

        Guild guild = discordUtil.getCurrentGuild();
        Member member = discordUtil.getMemberById(event.memberDiscordId());
        Role studyRole = discordUtil.findRoleById(event.studyDiscordRoleId());

        guild.removeRoleFromMember(member, studyRole).queue();
        attendanceRepository.deleteByStudyIdAndMemberId(event.studyId(), event.memberId());
        studyHistoryRepository.deleteByStudyIdAndStudentId(event.studyId(), event.memberId());
    }
}
