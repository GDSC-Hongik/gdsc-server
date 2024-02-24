package com.gdschongik.gdsc.domain.discord.domain;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash("discordVerificationCode")
public class DiscordVerificationCode {

    public static final int MIN_CODE_RANGE = 1000;
    public static final int MAX_CODE_RANGE = 9999;

    @Id
    private String discordUsername;

    private Integer code;

    @TimeToLive
    private Long ttl;

    @Builder
    private DiscordVerificationCode(String discordUsername, Integer code, Long ttl) {
        validateCodeRange(code);
        this.discordUsername = discordUsername;
        this.code = code;
        this.ttl = ttl;
    }

    private static void validateCodeRange(Integer code) {
        if (code < MIN_CODE_RANGE || code > MAX_CODE_RANGE) {
            throw new CustomException(ErrorCode.DISCORD_INVALID_CODE_RANGE);
        }
    }

    public static DiscordVerificationCode create(String discordUsername, Integer code, Long ttl) {
        return DiscordVerificationCode.builder()
                .discordUsername(discordUsername)
                .code(code)
                .ttl(ttl)
                .build();
    }

    public boolean matchesCode(Integer code) {
        return this.code.equals(code);
    }
}
