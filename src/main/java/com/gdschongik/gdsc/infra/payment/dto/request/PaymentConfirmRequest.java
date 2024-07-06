package com.gdschongik.gdsc.infra.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PaymentConfirmRequest(
        @NotBlank String paymentKey, @NotBlank @Size(min = 21, max = 21) String orderId, @Positive Long amount) {}
