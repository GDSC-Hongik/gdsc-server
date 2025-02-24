package com.gdschongik.gdsc.domain.coupon.dto.request;

import com.gdschongik.gdsc.domain.coupon.domain.CouponType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CouponCreateRequest(
        @NotBlank String name,
        @Positive BigDecimal discountAmount,
        @NotNull(message = "쿠폰 타입은 null이 될 수 없습니다.") CouponType couponType,
        @Nullable @Schema(description = "스터디 관련 쿠폰이 아니라면 null을 가집니다.") Long studyId) {}
