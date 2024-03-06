package com.gdschongik.gdsc.global.property;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "email")
public class EmailProperty {

    private final Gmail gmail;
    private final String host;
    private final int port;
    private final String protocol;
    private final String encoding;
    private final Map<String, Object> javaMailProperty;

    public record Gmail(String loginEmail, String password) {}
}
