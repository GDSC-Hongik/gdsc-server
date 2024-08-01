package com.gdschongik.gdsc.infra.feign.payment.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;

public class PaymentErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            var paymentErrorDto = objectMapper.readValue(response.body().asInputStream(), PaymentErrorDto.class);
            return new CustomPaymentException(response.status(), paymentErrorDto.code(), paymentErrorDto.message());
        } catch (IOException e) {
            return defaultErrorDecoder.decode(methodKey, response);
        }
    }
}
