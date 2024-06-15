package com.gdschongik.gdsc.domain.coupon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CouponCreateRequest(@NotBlank String name, @Positive BigDecimal discountAmount) {}
