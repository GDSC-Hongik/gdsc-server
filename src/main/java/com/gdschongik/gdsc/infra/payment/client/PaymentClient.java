package com.gdschongik.gdsc.infra.payment.client;

import com.gdschongik.gdsc.infra.payment.dto.request.PaymentConfirmRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "https://api.tosspayments.com")
public interface PaymentClient {

    @PostMapping("/v1/payments/confirm")
    void confirm(@RequestBody PaymentConfirmRequest request);
}
