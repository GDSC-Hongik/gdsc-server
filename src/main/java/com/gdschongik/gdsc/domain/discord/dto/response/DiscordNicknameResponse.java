package com.gdschongik.gdsc.domain.discord.dto.response;

public record DiscordNicknameResponse(String nickname) {

    public static DiscordNicknameResponse of(String nickname) {
        return new DiscordNicknameResponse(nickname);
    }
}
