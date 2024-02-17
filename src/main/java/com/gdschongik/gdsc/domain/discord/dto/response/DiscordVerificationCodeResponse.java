package com.gdschongik.gdsc.domain.discord.dto.response;

import java.time.Duration;

public record DiscordVerificationCodeResponse(Long code, Duration ttl) {

    public static DiscordVerificationCodeResponse of(Long code, Long ttlSeconds) {
        return new DiscordVerificationCodeResponse(code, Duration.ofSeconds(ttlSeconds));
    }
}
