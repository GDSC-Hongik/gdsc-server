package com.gdschongik.gdsc.domain.order.domain;

import com.gdschongik.gdsc.domain.common.model.BaseTimeEntity;
import com.gdschongik.gdsc.domain.common.vo.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
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
}
