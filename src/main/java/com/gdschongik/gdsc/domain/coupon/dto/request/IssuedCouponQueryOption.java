package com.gdschongik.gdsc.domain.coupon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record IssuedCouponQueryOption(
        @Schema(description = "학번") String studentId,
        @Schema(description = "이름") String memberName,
        @Schema(description = "전화번호") String phone,
        @Schema(description = "쿠폰 이름") String couponName,
        @Schema(description = "쿠폰 사용 여부") Boolean hasUsed,
        @Schema(description = "쿠폰 회수 여부") Boolean hasRevoked) {}
