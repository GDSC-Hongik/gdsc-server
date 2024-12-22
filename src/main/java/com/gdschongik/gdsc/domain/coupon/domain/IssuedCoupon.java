package com.gdschongik.gdsc.domain.coupon.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static java.lang.Boolean.*;

import com.gdschongik.gdsc.domain.common.model.BaseEntity;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssuedCoupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issued_coupon_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Comment("회수 여부")
    private Boolean hasRevoked;

    private LocalDateTime usedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private IssuedCoupon(Coupon coupon, Member member, Boolean hasRevoked) {
        this.coupon = coupon;
        this.member = member;
        this.hasRevoked = hasRevoked;
    }

    public static IssuedCoupon create(Coupon coupon, Member member) {
        return IssuedCoupon.builder()
                .coupon(coupon)
                .member(member)
                .hasRevoked(false)
                .build();
    }

    // 검증 로직

    public void validateUsable() {
        if (hasRevoked.equals(TRUE)) {
            throw new CustomException(COUPON_NOT_USABLE_REVOKED);
        }

        if (hasUsed()) {
            throw new CustomException(COUPON_NOT_USABLE_ALREADY_USED);
        }
    }

    private void validateRevokable() {
        if (hasRevoked.equals(TRUE)) {
            throw new CustomException(COUPON_NOT_REVOKABLE_ALREADY_REVOKED);
        }

        if (hasUsed()) {
            throw new CustomException(COUPON_NOT_REVOKABLE_ALREADY_USED);
        }
    }

    // 상태 변경 로직

    public void use(LocalDateTime now) {
        validateUsable();
        usedAt = now;
    }

    public void revoke() {
        validateRevokable();
        hasRevoked = true;
    }

    // 데이터 전달 로직

    public boolean hasUsed() {
        return usedAt != null;
    }

    public boolean isUsable() {
        try {
            validateUsable();
            return true;
        } catch (CustomException e) {
            return false;
        }
    }
}
