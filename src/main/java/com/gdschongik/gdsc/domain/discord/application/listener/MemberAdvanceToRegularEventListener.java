package com.gdschongik.gdsc.domain.discord.application.listener;

import com.gdschongik.gdsc.domain.discord.application.handler.MemberAdvanceToRegularEventHandler;
import com.gdschongik.gdsc.domain.member.domain.MemberAdvanceToRegularEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberAdvanceToRegularEventListener {

    private final MemberAdvanceToRegularEventHandler memberAdvanceToRegularEvenHandler;

    @TransactionalEventListener(MemberAdvanceToRegularEvent.class)
    public void handleMemberAdvanceToRegularEvent(MemberAdvanceToRegularEvent event) {
        memberAdvanceToRegularEvenHandler.delegate(event);
    }
}
