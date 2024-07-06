package com.gdschongik.gdsc.infra.payment.client;

import com.gdschongik.gdsc.infra.payment.config.BasicAuthConfig;
import com.gdschongik.gdsc.infra.payment.dto.request.PaymentConfirmRequest;
import com.gdschongik.gdsc.infra.payment.dto.response.PaymentResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paymentClient", url = "https://api.tosspayments.com", configuration = BasicAuthConfig.class)
public interface PaymentClient {

    @PostMapping("/v1/payments/confirm")
    PaymentResponse confirm(@Valid @RequestBody PaymentConfirmRequest request);
}
