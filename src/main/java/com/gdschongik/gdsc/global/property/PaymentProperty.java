package com.gdschongik.gdsc.global.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "toss")
public class PaymentProperty {
    private final String secretKey;
}
