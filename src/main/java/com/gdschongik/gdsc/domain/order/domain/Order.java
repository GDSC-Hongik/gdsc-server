package com.gdschongik.gdsc.domain.order.domain;

import com.gdschongik.gdsc.domain.common.model.BaseEntity;
import com.gdschongik.gdsc.domain.coupon.domain.IssuedCoupon;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Table(name = "orders")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long id;

    @Comment("주문상태")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Comment("주문 nanoId")
    @Column(unique = true, length = 21)
    private String nanoId;

    @Comment("주문자 ID")
    private Long memberId;

    @Comment("주문 대상 멤버십 ID")
    private Long membershipId;

    @Comment("신청하려는 리쿠르팅 ID")
    private Long recruitmentId;

    @Comment("사용하려는 발급쿠폰 ID")
    private Long issuedCouponId;

    @Embedded
    private MoneyInfo moneyInfo;

    @Builder(access = AccessLevel.PRIVATE)
    private Order(
            OrderStatus status,
            String nanoId,
            Long memberId,
            Long membershipId,
            Long recruitmentId,
            Long issuedCouponId,
            MoneyInfo moneyInfo) {
        this.status = status;
        this.nanoId = nanoId;
        this.memberId = memberId;
        this.membershipId = membershipId;
        this.recruitmentId = recruitmentId;
        this.issuedCouponId = issuedCouponId;
        this.moneyInfo = moneyInfo;
    }

    /**
     * 결제 요청 전 임시 주문을 생성합니다.
     * 쿠폰의 경우 사용 여부를 선택할 수 있습니다.
     */
    public static Order createPending(
            String nanoId, Membership membership, @Nullable IssuedCoupon issuedCoupon, MoneyInfo moneyInfo) {
        return Order.builder()
                .status(OrderStatus.PENDING)
                .nanoId(nanoId)
                .memberId(membership.getMember().getId())
                .membershipId(membership.getId())
                .recruitmentId(membership.getRecruitment().getId())
                .issuedCouponId(issuedCoupon != null ? issuedCoupon.getId() : null)
                .moneyInfo(moneyInfo)
                .build();
    }
}
