package com.gdschongik.gdsc.domain.coupon.dto.request;

import java.util.List;

public record CouponIssueRequest(Long couponId, List<Long> memberIds) {}
