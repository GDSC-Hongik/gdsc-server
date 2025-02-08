package com.gdschongik.gdsc.domain.coupon.dto.response;

import com.gdschongik.gdsc.domain.coupon.domain.CouponType;

public record CouponTypeResponse(CouponType couponType, String name) {
    public static CouponTypeResponse from(CouponType couponType) {
        return new CouponTypeResponse(couponType, couponType.getValue());
    }
}
