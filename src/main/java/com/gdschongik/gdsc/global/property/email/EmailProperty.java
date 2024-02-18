package com.gdschongik.gdsc.global.property.email;

import com.gdschongik.gdsc.global.util.Pair;
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
    private final String encoding;
    private final JavaMailProperty javaMailProperty;

    public record Gmail(String loginEmail, String password) {}

    public record JavaMailProperty(Pair<Boolean> smtpAuth, SocketFactory socketFactory) {}

    public record SocketFactory(Pair<Integer> port, Pair<Boolean> fallback, Pair<String> classInfo) {}
}
