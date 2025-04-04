package com.gdschongik.gdsc.domain.discord.application.listener;

import com.gdschongik.gdsc.domain.discord.application.handler.MemberDiscordRoleRevokeHandler;
import com.gdschongik.gdsc.domain.member.domain.event.MemberDemotedToAssociateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberDemotedToAssociateEventListener {

    private final MemberDiscordRoleRevokeHandler memberDiscordRoleRevokeHandler;

    @ApplicationModuleListener
    public void demoteMemberToAssociate(MemberDemotedToAssociateEvent event) {
        log.info(
                "[MemberDemotedToAssociateEventListener] 회원 준회원 강등 이벤트 수신: memberId={}, discordId={}",
                event.memberId(),
                event.discordId());
        memberDiscordRoleRevokeHandler.delegate(event);
    }
}
