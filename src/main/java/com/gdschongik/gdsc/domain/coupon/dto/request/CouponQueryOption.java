package com.gdschongik.gdsc.domain.coupon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CouponQueryOption(@Schema(description = "쿠폰 이름") String couponName) {}
