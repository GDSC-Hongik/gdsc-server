package com.gdschongik.gdsc.domain.coupon.dto.request;

import static com.gdschongik.gdsc.global.common.constant.RegexConstant.PHONE_WITHOUT_HYPHEN;
import static com.gdschongik.gdsc.global.common.constant.RegexConstant.STUDENT_ID;

import io.swagger.v3.oas.annotations.media.Schema;

public record IssuedCouponQueryOption(
        @Schema(description = "학번", pattern = STUDENT_ID) String studentId,
        @Schema(description = "이름") String memberName,
        @Schema(description = "전화번호", pattern = PHONE_WITHOUT_HYPHEN) String phone,
        @Schema(description = "쿠폰 이름") String couponName,
        @Schema(description = "쿠폰 사용 여부") boolean isUsed,
        @Schema(description = "쿠폰 회수 여부") boolean isRevoked) {}
