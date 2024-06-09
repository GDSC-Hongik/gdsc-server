package com.gdschongik.gdsc.domain.member.application.listener;

import com.gdschongik.gdsc.domain.member.application.handler.MemberAssociateEventHandler;
import com.gdschongik.gdsc.domain.member.domain.MemberAssociateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MemberAssociateEventListener {

    private final MemberAssociateEventHandler memberAssociateEventHandler;

    @TransactionalEventListener(MemberAssociateEvent.class)
    public void handleMemberAssociateEvent(MemberAssociateEvent event) {
        memberAssociateEventHandler.advanceToAssociate(event);
    }
}
