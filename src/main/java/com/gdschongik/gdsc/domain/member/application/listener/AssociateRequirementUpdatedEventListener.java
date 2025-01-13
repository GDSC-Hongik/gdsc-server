package com.gdschongik.gdsc.domain.member.application.listener;

import com.gdschongik.gdsc.domain.member.application.handler.AssociateRequirementUpdatedEventHandler;
import com.gdschongik.gdsc.domain.member.domain.AssociateRequirementUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AssociateRequirementUpdatedEventListener {

    private final AssociateRequirementUpdatedEventHandler associateRequirementUpdatedEventHandler;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleAssociateRequirementUpdatedEvent(AssociateRequirementUpdatedEvent event) {
        associateRequirementUpdatedEventHandler.advanceToAssociate(event);
    }
}
