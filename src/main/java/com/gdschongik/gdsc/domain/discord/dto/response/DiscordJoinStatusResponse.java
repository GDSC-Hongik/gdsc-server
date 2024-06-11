package com.gdschongik.gdsc.domain.discord.dto.response;

public record DiscordJoinStatusResponse(boolean joinStatus) {
    public static DiscordJoinStatusResponse from(boolean joinStatus) {
        return new DiscordJoinStatusResponse(joinStatus);
    }
}
