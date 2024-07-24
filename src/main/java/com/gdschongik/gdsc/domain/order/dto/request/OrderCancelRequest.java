package com.gdschongik.gdsc.domain.order.dto.request;

import jakarta.validation.constraints.NotBlank;

public record OrderCancelRequest(@NotBlank String cancelReason) {}
