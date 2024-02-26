package com.gdschongik.gdsc.domain.discord.dto.response;

import java.time.Duration;

public record DiscordVerificationCodeResponse(Integer code, Duration ttl) {

    public static DiscordVerificationCodeResponse of(Integer code, Long ttlSeconds) {
        return new DiscordVerificationCodeResponse(code, Duration.ofSeconds(ttlSeconds));
    }
}
