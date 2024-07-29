package com.gdschongik.gdsc.domain.order.application;

import com.gdschongik.gdsc.domain.membership.application.MembershipService;
import com.gdschongik.gdsc.domain.order.domain.OrderCompletedEvent;
import com.gdschongik.gdsc.domain.order.domain.OrderFreeCreatedEvent;
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
    public void handleOrderFreeCreatedEvent(OrderFreeCreatedEvent orderFreeCreatedEvent) {
        log.debug("[OrderEventHandler] 무료 주문 생성 이벤트 수신: nanoId={}", orderFreeCreatedEvent.nanoId());
        membershipService.verifyPaymentStatus(orderFreeCreatedEvent.nanoId());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOrderCompletedEvent(OrderCompletedEvent orderCompletedEvent) {
        log.debug("[OrderEventHandler] 주문 완료 이벤트 수신: nanoId={}", orderCompletedEvent.nanoId());
        membershipService.verifyPaymentStatus(orderCompletedEvent.nanoId());
    }
}
