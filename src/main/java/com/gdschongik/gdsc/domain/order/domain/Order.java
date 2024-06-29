package com.gdschongik.gdsc.domain.order.domain;

import com.gdschongik.gdsc.domain.common.model.BaseTimeEntity;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.coupon.domain.IssuedCoupon;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Comment("주문상태")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Comment("주문 nanoId")
    @Column(unique = true, length = 21)
    private String nanoId;

    @Comment("주문자 ID")
    private Long memberId;

    @Comment("신청하려는 리쿠르팅 ID")
    private Long recruitmentId;

    @Comment("사용한 발급쿠폰 ID")
    private Long issuedCouponId;

    @Comment("주문총액")
    @Embedded
    private Money totalAmount;

    @Comment("쿠폰할인금액")
    @Embedded
    private Money discountAmount;

    @Comment("최종결제금액")
    @Embedded
    private Money finalPaymentAmount;

    @Builder(access = AccessLevel.PRIVATE)
    private Order(
            OrderStatus status,
            String nanoId,
            Long memberId,
            Long recruitmentId,
            Long issuedCouponId,
            Money totalAmount,
            Money discountAmount,
            Money finalPaymentAmount) {
        this.status = status;
        this.nanoId = nanoId;
        this.memberId = memberId;
        this.recruitmentId = recruitmentId;
        this.issuedCouponId = issuedCouponId;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalPaymentAmount = finalPaymentAmount;
    }

    /**
     * 결제 요청 전 임시 주문을 생성합니다.
     */
    public static Order createPending(
            String nanoId,
            Member member,
            Recruitment recruitment,
            IssuedCoupon issuedCoupon,
            Money totalAmount,
            Money discountAmount,
            Money finalPaymentAmount) {
        return Order.builder()
                .status(OrderStatus.PENDING)
                .nanoId(nanoId)
                .memberId(member.getId())
                .recruitmentId(recruitment.getId())
                .issuedCouponId(issuedCoupon.getId())
                .totalAmount(totalAmount)
                .discountAmount(discountAmount)
                .finalPaymentAmount(finalPaymentAmount)
                .build();
    }
}
