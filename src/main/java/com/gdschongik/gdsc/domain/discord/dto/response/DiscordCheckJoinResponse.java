package com.gdschongik.gdsc.domain.discord.dto.response;

public record DiscordCheckJoinResponse(boolean isJoined) {
    public static DiscordCheckJoinResponse from(boolean isJoined) {
        return new DiscordCheckJoinResponse(isJoined);
    }
}
