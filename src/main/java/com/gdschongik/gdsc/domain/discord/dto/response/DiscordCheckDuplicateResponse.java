package com.gdschongik.gdsc.domain.discord.dto.response;

public record DiscordCheckDuplicateResponse(Boolean isDuplicate) {
    public static DiscordCheckDuplicateResponse from(Boolean isDuplicate) {
        return new DiscordCheckDuplicateResponse(isDuplicate);
    }
}
