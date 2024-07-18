package com.gdschongik.gdsc.infra.feign.payment.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomPaymentException extends RuntimeException {
    private final int status;
    private final String code;
    private final String message;
}
