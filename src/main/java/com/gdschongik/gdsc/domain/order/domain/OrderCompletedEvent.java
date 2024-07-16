package com.gdschongik.gdsc.domain.order.domain;

public record OrderCompletedEvent(Long orderId) {
    // TODO: 주문 완료 후 결제상태 변경 및 정회원 승급 검사
}
