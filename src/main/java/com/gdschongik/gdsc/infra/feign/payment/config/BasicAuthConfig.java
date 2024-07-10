package com.gdschongik.gdsc.infra.feign.payment.config;

import com.gdschongik.gdsc.global.property.PaymentProperty;
import feign.auth.BasicAuthRequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
public class BasicAuthConfig {

    private final PaymentProperty paymentProperty;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(paymentProperty.getSecretKey(), "");
    }
}
