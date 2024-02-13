package com.gdschongik.gdsc.domain.discord.application;

import static com.gdschongik.gdsc.domain.discord.domain.DiscordVerificationCode.*;

import com.gdschongik.gdsc.domain.discord.dao.DiscordVerificationCodeRepository;
import com.gdschongik.gdsc.domain.discord.domain.DiscordVerificationCode;
import com.gdschongik.gdsc.domain.discord.dto.response.DiscordVerificationCodeResponse;
import com.gdschongik.gdsc.domain.member.domain.Member;
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

    private final MemberUtil memberUtil;
    private final DiscordVerificationCodeRepository discordVerificationCodeRepository;

    @Transactional
    public DiscordVerificationCodeResponse createVerificationCode() {
        Member member = memberUtil.getCurrentMember();

        String discordUsername = member.getDiscordUsername();
        Long code = generateRandomCode();
        DiscordVerificationCode discordVerificationCode =
                DiscordVerificationCode.create(discordUsername, code, DISCORD_CODE_TTL_SECONDS);

        discordVerificationCodeRepository.save(discordVerificationCode);

        return DiscordVerificationCodeResponse.of(code);
    }

    @SneakyThrows
    private static Long generateRandomCode() {
        return SecureRandom.getInstanceStrong()
                .longs(MIN_CODE_RANGE, MAX_CODE_RANGE + 1)
                .findFirst()
                .orElseThrow();
    }

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
