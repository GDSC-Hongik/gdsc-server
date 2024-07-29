package com.gdschongik.gdsc.domain.order.application;

import com.gdschongik.gdsc.domain.membership.application.MembershipService;
import com.gdschongik.gdsc.domain.order.domain.OrderCompletedEvent;
import com.gdschongik.gdsc.domain.order.domain.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventHandler {

    private final MembershipService membershipService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
        log.info(
                "[OrderEventHandler] 주문 생성 이벤트 수신: nanoId={}, isFree={}",
                orderCreatedEvent.nanoId(),
                orderCreatedEvent.isFree());
        if (orderCreatedEvent.isFree()) {
            membershipService.verifyPaymentStatus(orderCreatedEvent.nanoId());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOrderCompletedEvent(OrderCompletedEvent orderCompletedEvent) {
        log.info("[OrderEventHandler] 주문 완료 이벤트 수신: nanoId={}", orderCompletedEvent.nanoId());
        membershipService.verifyPaymentStatus(orderCompletedEvent.nanoId());
    }
}
