package com.gdschongik.gdsc.domain.discord.dto.response;

public record DiscordVerificationCodeResponse(Long code) {

    public static DiscordVerificationCodeResponse of(Long code) {
        return new DiscordVerificationCodeResponse(code);
    }
}
