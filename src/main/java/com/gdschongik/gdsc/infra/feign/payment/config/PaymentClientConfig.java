package com.gdschongik.gdsc.infra.feign.payment.config;

import com.gdschongik.gdsc.infra.feign.payment.error.PaymentErrorDecoder;
import org.springframework.context.annotation.Import;

@Import({BasicAuthConfig.class, PaymentErrorDecoder.class})
public class PaymentClientConfig {}
