package com.gdschongik.gdsc.domain.discord.application.listener;

import com.gdschongik.gdsc.domain.discord.application.handler.DelegateMemberDiscordEventHandler;
import com.gdschongik.gdsc.domain.member.domain.MemberRegularEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class DelegateMemberDiscordEventListener {

    private final DelegateMemberDiscordEventHandler delegateMemberDiscordEventHandler;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void delegateMemberDiscordEvent(MemberRegularEvent event) {
        log.info("[DelegateMemberDiscordEventListener] 정회원 승급 이벤트 수신: memberId={}", event.memberId());
        delegateMemberDiscordEventHandler.delegate(event);
    }
}
