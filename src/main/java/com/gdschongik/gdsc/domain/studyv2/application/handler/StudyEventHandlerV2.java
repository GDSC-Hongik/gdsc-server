package com.gdschongik.gdsc.domain.studyv2.application.handler;

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

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleStudyApplyCompletedEvent(StudyApplyCompletedEvent event) {
        Guild guild = discordUtil.getCurrentGuild();
        Member member = discordUtil.getMemberById(event.studentDiscordId());
        Role studyRole = discordUtil.findRoleById(event.studyDiscordRoleId());

        guild.addRoleToMember(member, studyRole).queue();

        log.info(
                "[StudyApplyCompletedEvent] 디스코드 스터디 역할 부여 완료: memberDiscordId={}, studyDiscordRoleId={}",
                event.studentDiscordId(),
                event.studyDiscordRoleId());
    }
}
