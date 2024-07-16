package com.gdschongik.gdsc.infra.feign.payment.client;

import com.gdschongik.gdsc.infra.feign.payment.config.BasicAuthConfig;
import com.gdschongik.gdsc.infra.feign.payment.dto.request.PaymentConfirmRequest;
import com.gdschongik.gdsc.infra.feign.payment.dto.response.PaymentResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paymentClient", url = "https://api.tosspayments.com", configuration = BasicAuthConfig.class)
public interface PaymentClient {

    // TODO: Feign 예외 처리 구현

    @PostMapping("/v1/payments/confirm")
    PaymentResponse confirm(@Valid @RequestBody PaymentConfirmRequest request);
}
