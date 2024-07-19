package com.gdschongik.gdsc.infra.feign.payment.client;

import com.gdschongik.gdsc.infra.feign.payment.config.PaymentClientConfig;
import com.gdschongik.gdsc.infra.feign.payment.dto.request.PaymentConfirmRequest;
import com.gdschongik.gdsc.infra.feign.payment.dto.response.PaymentResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paymentClient", url = "https://api.tosspayments.com", configuration = PaymentClientConfig.class)
public interface PaymentClient {

    @PostMapping("/v1/payments/confirm")
    PaymentResponse confirm(@Valid @RequestBody PaymentConfirmRequest request);
}
