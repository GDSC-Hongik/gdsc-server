package com.gdschongik.gdsc.infra.feign.payment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PaymentCancelRequest(@NotBlank String cancelReason) {}
