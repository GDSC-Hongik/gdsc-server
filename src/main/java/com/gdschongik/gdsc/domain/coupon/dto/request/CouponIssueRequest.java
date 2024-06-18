package com.gdschongik.gdsc.domain.coupon.dto.request;

import jakarta.validation.constraints.Positive;

public record CouponIssueRequest(@Positive Long couponId) {}
