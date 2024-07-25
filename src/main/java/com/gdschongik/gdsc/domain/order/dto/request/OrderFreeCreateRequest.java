package com.gdschongik.gdsc.domain.order.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record OrderFreeCreateRequest(
        @Size(min = 21, max = 21) String orderNanoId,
        @NotNull @Positive Long membershipId,
        @Nullable @Positive Long issuedCouponId) {}
