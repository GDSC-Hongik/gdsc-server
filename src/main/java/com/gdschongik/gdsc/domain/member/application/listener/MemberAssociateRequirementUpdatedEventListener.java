package com.gdschongik.gdsc.domain.member.application.listener;

import com.gdschongik.gdsc.domain.member.application.handler.MemberAssociateRequirementUpdatedEventHandler;
import com.gdschongik.gdsc.domain.member.domain.event.MemberAssociateRequirementUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MemberAssociateRequirementUpdatedEventListener {

    private final MemberAssociateRequirementUpdatedEventHandler memberAssociateRequirementUpdatedEventHandler;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleAssociateRequirementUpdatedEvent(MemberAssociateRequirementUpdatedEvent event) {
        memberAssociateRequirementUpdatedEventHandler.advanceToAssociate(event);
    }
}
