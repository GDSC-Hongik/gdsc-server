package com.gdschongik.gdsc.domain.order.dto.response;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.order.domain.MoneyInfo;
import com.gdschongik.gdsc.domain.order.domain.Order;
import com.gdschongik.gdsc.domain.order.domain.OrderStatus;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.global.util.formatter.MoneyFormatter;
import com.gdschongik.gdsc.global.util.formatter.SemesterFormatter;
import com.querydsl.core.annotations.QueryProjection;
import java.time.ZonedDateTime;

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
        ZonedDateTime approvedAt) {

    @QueryProjection
    public OrderAdminResponse(
            Long orderId,
            Integer academicYear,
            SemesterType semesterType,
            String memberName,
            OrderStatus status,
            String studentId,
            String nanoId,
            String paymentKey,
            Money totalAmount,
            Money discountAmount,
            Money finalPaymentAmount,
            ZonedDateTime approvedAt) {
        this(
                orderId,
                SemesterFormatter.format(academicYear, semesterType),
                memberName,
                status,
                studentId,
                nanoId,
                paymentKey,
                MoneyFormatter.format(totalAmount),
                MoneyFormatter.format(discountAmount),
                MoneyFormatter.format(finalPaymentAmount),
                approvedAt);
    }

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
                order.getApprovedAt());
    }
}
