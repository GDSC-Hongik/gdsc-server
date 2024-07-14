package com.gdschongik.gdsc.domain.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record OrderCompleteRequest(
        @NotBlank String paymentKey, @NotBlank @Size(min = 21, max = 21) String orderNanoId, @Positive Long amount) {}
