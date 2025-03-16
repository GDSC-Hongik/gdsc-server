package com.gdschongik.gdsc.global.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "modulith")
public class ModulithProperty {

    private final int retryIntervalSecond;
    private final int dlqIntervalMinute;
    private final int minAge;
    private final int maxAge;
}
