package com.gdschongik.gdsc.domain.discord.application.listener;

import com.gdschongik.gdsc.domain.discord.application.handler.DelegateMemberDiscordEventHandler;
import com.gdschongik.gdsc.domain.member.domain.MemberRegularEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DelegateMemberDiscordEventListener {

    private final DelegateMemberDiscordEventHandler delegateMemberDiscordEventHandler;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void delegateMemberDiscordEvent(MemberRegularEvent event) {
        delegateMemberDiscordEventHandler.delegate(event);
    }
}
