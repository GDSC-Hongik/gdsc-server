package com.gdschongik.gdsc.domain.discord.application;

import static com.gdschongik.gdsc.domain.discord.domain.DiscordVerificationCode.*;

import com.gdschongik.gdsc.domain.discord.dao.DiscordVerificationCodeRepository;
import com.gdschongik.gdsc.domain.discord.domain.DiscordVerificationCode;
import com.gdschongik.gdsc.domain.discord.dto.response.DiscordVerificationCodeResponse;
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

    @Transactional
    public DiscordVerificationCodeResponse createVerificationCode(String discordUsername) {

        Long code = generateRandomCode();
        DiscordVerificationCode discordVerificationCode =
                DiscordVerificationCode.create(discordUsername, code, DISCORD_CODE_TTL_SECONDS);

        discordVerificationCodeRepository.save(discordVerificationCode);

        return DiscordVerificationCodeResponse.of(code, DISCORD_CODE_TTL_SECONDS);
    }

    @SneakyThrows
    private static Long generateRandomCode() {
        return SecureRandom.getInstanceStrong()
                .longs(MIN_CODE_RANGE, MAX_CODE_RANGE + 1L)
                .findFirst()
                .orElseThrow();
    }

    // TODO: 디스코드 연동하기 피쳐에서 구현
    public void verifyDiscordCode(String discordUsername, String code) {
        DiscordVerificationCode discordVerificationCode =
                discordVerificationCodeRepository.findById(discordUsername).orElseThrow();

        // TODO: 4자리 숫자의 문자열로 비교
        if (!discordVerificationCode.getCode().toString().equals(code)) {
            // TODO: throw exception
        }

        discordVerificationCodeRepository.delete(discordVerificationCode);
    }
}
