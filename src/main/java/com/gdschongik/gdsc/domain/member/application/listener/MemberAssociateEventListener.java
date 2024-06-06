package com.gdschongik.gdsc.domain.member.application.listener;

import com.gdschongik.gdsc.domain.member.application.handler.MemberAssociateEventHandler;
import com.gdschongik.gdsc.domain.member.domain.MemberAssociateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberAssociateEventListener {

    private final MemberAssociateEventHandler memberAssociateEventHandler;

    @TransactionalEventListener(MemberAssociateEvent.class)
    public void handleMemberAssociateEvent(MemberAssociateEvent event) {
        memberAssociateEventHandler.handle(event);
    }
}
