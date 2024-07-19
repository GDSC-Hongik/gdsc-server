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
        if (!discordVerificationCode.matchesCode(code)) {
            throw new CustomException(DISCORD_CODE_MISMATCH);
        }

        if (isDiscordUsernameDuplicate) {
            throw new CustomException(MEMBER_DISCORD_USERNAME_DUPLICATE);
        }

        if (isNicknameDuplicate) {
            throw new CustomException(MEMBER_NICKNAME_DUPLICATE);
        }
    }
}
