package com.gdschongik.gdsc.domain.coupon.dto.response;

import com.gdschongik.gdsc.domain.coupon.domain.Coupon;
import java.math.BigDecimal;

public record CouponResponse(Long couponId, String name, BigDecimal discountAmount) {
    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(), coupon.getName(), coupon.getDiscountAmount().getAmount());
    }
}
