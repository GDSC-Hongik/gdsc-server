package com.gdschongik.gdsc.domain.discord.application.listener;

import com.gdschongik.gdsc.domain.discord.application.handler.MemberAdvanceToRegularEventHandler;
import com.gdschongik.gdsc.domain.member.domain.MemberAdvanceToRegularEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MemberAdvanceToRegularEventListener {

    private final MemberAdvanceToRegularEventHandler memberAdvanceToRegularEvenHandler;

    @TransactionalEventListener(MemberAdvanceToRegularEvent.class)
    public void handleMemberAdvanceToRegularEvent(MemberAdvanceToRegularEvent event) {
        memberAdvanceToRegularEvenHandler.delegate(event);
    }
}
