package com.gdschongik.gdsc.domain.coupon.domain;

import com.gdschongik.gdsc.domain.common.model.BaseTimeEntity;
import com.gdschongik.gdsc.domain.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.data.annotation.Id;

public class IssuedCoupon extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "issued_coupon_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime usedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private IssuedCoupon(Coupon coupon, Member member, LocalDateTime usedAt) {
        this.coupon = coupon;
        this.member = member;
        this.usedAt = usedAt;
    }

    public static IssuedCoupon issue(Coupon coupon, Member member) {
        return IssuedCoupon.builder().coupon(coupon).member(member).build();
    }

    public void useCoupon() {
        this.usedAt = LocalDateTime.now();
    }

    public boolean isUsed() {
        return this.usedAt != null;
    }
}
