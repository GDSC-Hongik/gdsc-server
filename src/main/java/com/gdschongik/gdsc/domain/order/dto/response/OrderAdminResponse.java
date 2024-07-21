package com.gdschongik.gdsc.domain.order.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.order.domain.MoneyInfo;
import com.gdschongik.gdsc.domain.order.domain.Order;
import com.gdschongik.gdsc.domain.order.domain.OrderStatus;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.global.util.formatter.MoneyFormatter;
import com.gdschongik.gdsc.global.util.formatter.SemesterFormatter;
import java.time.LocalDateTime;

public record OrderAdminResponse(
        Long orderId,
        String semester,
        String memberName,
        OrderStatus status,
        String studentId,
        String nanoId,
        String paymentKey,
        String totalAmount,
        String discountAmount,
        String finalPaymentAmount,
        LocalDateTime updatedAt) {
    public static OrderAdminResponse from(Order order, Recruitment recruitment, Member member) {
        MoneyInfo moneyInfo = order.getMoneyInfo();
        return new OrderAdminResponse(
                order.getId(),
                SemesterFormatter.format(recruitment),
                member.getName(),
                order.getStatus(),
                member.getStudentId(),
                order.getNanoId(),
                order.getPaymentKey(),
                MoneyFormatter.format(moneyInfo.getTotalAmount()),
                MoneyFormatter.format(moneyInfo.getDiscountAmount()),
                MoneyFormatter.format(moneyInfo.getFinalPaymentAmount()),
                order.getUpdatedAt());
    }
}
