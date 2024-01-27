package com.gdschongik.gdsc.global.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtProperty {

    private final TokenProperty accessToken;
    private final TokenProperty refreshToken;
    private final String issuer;

    public record TokenProperty(String secret, Long expirationTime) {}
}
