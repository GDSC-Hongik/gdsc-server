package com.gdschongik.gdsc.domain.coupon.dto.response;

import com.gdschongik.gdsc.domain.coupon.domain.IssuedCoupon;
import com.gdschongik.gdsc.domain.member.dto.MemberDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IssuedCouponResponse(
        Long issuedCouponId,
        MemberDto member,
        String couponName,
        BigDecimal discountAmount,
        LocalDateTime usedAt,
        LocalDateTime issuedAt,
        Boolean isUsed,
        Boolean isRevoked) {
    public static IssuedCouponResponse from(IssuedCoupon issuedCoupon) {
        return new IssuedCouponResponse(
                issuedCoupon.getId(),
                MemberDto.from(issuedCoupon.getMember()),
                issuedCoupon.getCoupon().getName(),
                issuedCoupon.getCoupon().getDiscountAmount().getAmount(),
                issuedCoupon.getUsedAt(),
                issuedCoupon.getCreatedAt(),
                issuedCoupon.isUsed(),
                issuedCoupon.isRevoked());
    }
}
