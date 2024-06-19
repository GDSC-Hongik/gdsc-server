package com.gdschongik.gdsc.domain.discord.application.listener;

import com.gdschongik.gdsc.domain.discord.application.handler.DelegateMemberDiscordEventHandler;
import com.gdschongik.gdsc.domain.member.domain.MemberRegularEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DelegateMemberEventListener {

    private final DelegateMemberDiscordEventHandler delegateMemberDiscordEventHandler;

    @TransactionalEventListener(classes = MemberRegularEvent.class)
    public void delegateMemberDiscordEvent(MemberRegularEvent event) {
        delegateMemberDiscordEventHandler.delegate(event);
    }
}
