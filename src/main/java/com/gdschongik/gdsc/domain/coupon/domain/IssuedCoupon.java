package com.gdschongik.gdsc.domain.coupon.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static java.lang.Boolean.*;

import com.gdschongik.gdsc.domain.common.model.BaseTimeEntity;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import org.hibernate.annotations.Comment;
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

    @Comment("회수 여부")
    private Boolean isRevoked;

    private LocalDateTime usedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private IssuedCoupon(Coupon coupon, Member member, Boolean isRevoked) {
        this.coupon = coupon;
        this.member = member;
        this.isRevoked = isRevoked;
    }

    public static IssuedCoupon issue(Coupon coupon, Member member) {
        return IssuedCoupon.builder()
                .coupon(coupon)
                .member(member)
                .isRevoked(false)
                .build();
    }

    public void useCoupon() {
        validateUsable();
        this.usedAt = LocalDateTime.now();
    }

    private void validateUsable() {
        if (this.isRevoked.equals(FALSE)) {
            throw new CustomException(COUPON_NOT_USABLE);
        }

        if (isUsed()) {
            throw new CustomException(COUPON_ALREADY_USED);
        }
    }

    public boolean isUsed() {
        return this.usedAt != null;
    }
}
