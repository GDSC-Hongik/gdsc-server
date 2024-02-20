package com.gdschongik.gdsc.domain.discord.application;

import static com.gdschongik.gdsc.domain.discord.domain.DiscordVerificationCode.*;

import com.gdschongik.gdsc.domain.discord.dao.DiscordVerificationCodeRepository;
import com.gdschongik.gdsc.domain.discord.domain.DiscordVerificationCode;
import com.gdschongik.gdsc.domain.discord.dto.request.DiscordLinkRequest;
import com.gdschongik.gdsc.domain.discord.dto.response.DiscordVerificationCodeResponse;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OnboardingDiscordService {

    public static final long DISCORD_CODE_TTL_SECONDS = 300L;

    private final DiscordVerificationCodeRepository discordVerificationCodeRepository;
    private final MemberUtil memberUtil;

    @Transactional
    public DiscordVerificationCodeResponse createVerificationCode(String discordUsername) {

        Integer code = generateRandomCode();
        DiscordVerificationCode discordVerificationCode =
                DiscordVerificationCode.create(discordUsername, code, DISCORD_CODE_TTL_SECONDS);

        discordVerificationCodeRepository.save(discordVerificationCode);

        return DiscordVerificationCodeResponse.of(code, DISCORD_CODE_TTL_SECONDS);
    }

    @SneakyThrows
    private static Integer generateRandomCode() {
        return SecureRandom.getInstanceStrong()
                .ints(MIN_CODE_RANGE, MAX_CODE_RANGE + 1)
                .findFirst()
                .orElseThrow();
    }

    @Transactional
    public void verifyDiscordCode(DiscordLinkRequest request) {
        DiscordVerificationCode discordVerificationCode = discordVerificationCodeRepository
                .findById(request.discordUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.DISCORD_CODE_NOT_FOUND));

        validateDiscordCodeMatches(request, discordVerificationCode);

        discordVerificationCodeRepository.delete(discordVerificationCode);

        final Member currentMember = memberUtil.getCurrentMember();
        currentMember.verifyDiscord(request.discordUsername(), request.discordUsername());
    }

    private void validateDiscordCodeMatches(
            DiscordLinkRequest request, DiscordVerificationCode discordVerificationCode) {
        if (!discordVerificationCode.getCode().equals(request.code())) {
            throw new CustomException(ErrorCode.DISCORD_CODE_MISMATCH);
        }
    }
}
