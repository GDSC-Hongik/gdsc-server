package com.gdschongik.gdsc.domain.discord.application.listener;

import com.gdschongik.gdsc.domain.discord.application.handler.MemberDiscordRoleRevokeHandler;
import com.gdschongik.gdsc.domain.member.domain.MemberDemotedToAssociateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberDemotedToAssociateEventListener {

    private final MemberDiscordRoleRevokeHandler memberDiscordRoleRevokeHandler;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void demoteMemberToAssociate(MemberDemotedToAssociateEvent event) {
        log.info(
                "[MemberDemotedToAssociateEventListener] 회원 준회원 강등 이벤트 수신: memberId={}, discordId={}",
                event.memberId(),
                event.discordId());
        memberDiscordRoleRevokeHandler.delegate(event);
    }
}
