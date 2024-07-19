package com.gdschongik.gdsc.domain.discord.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;

@DomainService
public class DiscordValidator {

    public void validateVerifyDiscordCode(
            Integer code,
            DiscordVerificationCode discordVerificationCode,
            boolean isDiscordUsernameDuplicate,
            boolean isNicknameDuplicate) {
        // 입력받은 코드가 일치하는지 검증
        if (!discordVerificationCode.matchesCode(code)) {
            throw new CustomException(DISCORD_CODE_MISMATCH);
        }

        // 디스코드 유저네임이 중복되는지 검증
        if (isDiscordUsernameDuplicate) {
            throw new CustomException(MEMBER_DISCORD_USERNAME_DUPLICATE);
        }

        // 닉네임이 중복되는지 검증
        if (isNicknameDuplicate) {
            throw new CustomException(MEMBER_NICKNAME_DUPLICATE);
        }
    }
}
