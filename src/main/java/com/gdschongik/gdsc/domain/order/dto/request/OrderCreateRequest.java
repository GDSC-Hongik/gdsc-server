package com.gdschongik.gdsc.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record OrderCreateRequest(
        @Size(min = 21, max = 21) String orderNanoId,
        @NotNull @Positive Long memberId,
        @NotNull @Positive Long recruitmentId,
        @Positive Long issuedCouponId,
        @NotNull BigDecimal totalAmount,
        @NotNull BigDecimal discountAmount,
        @NotNull BigDecimal finalPaymentAmount) {}
