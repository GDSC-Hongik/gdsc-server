package com.gdschongik.gdsc.global.property;

import com.gdschongik.gdsc.domain.auth.domain.TokenType;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtProperty {

    private final Map<TokenType, TokenProperty> token;
    private final String issuer;

    public record TokenProperty(String secret, Long expirationTime) {
        public Long expirationMilliTime() {
            return expirationTime * 1000;
        }
    }
}
