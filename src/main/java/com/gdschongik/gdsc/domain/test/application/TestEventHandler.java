package com.gdschongik.gdsc.domain.test.application;

import com.gdschongik.gdsc.domain.test.domain.MyMembership;
import com.gdschongik.gdsc.domain.test.domain.MyOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestEventHandler {

    private final TestService testService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOrderConfirmedEvent(MyOrder.OrderConfirmedEvent event) {
        log.info("Order confirmed event received: {}", event);
        testService.verifyMembership(event.orderId());
        log.info("Order confirmed event handled: {}", event);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleMembershipVerifiedEvent(MyMembership.MembershipVerifiedEvent event) {
        log.info("Membership verified event received: {}", event);
        testService.advanceMember(event.membershipId());
        log.info("Membership verified event handled: {}", event);
    }
}
