package com.gdschongik.gdsc.domain.discord.api;

import com.gdschongik.gdsc.domain.discord.application.OnboardingDiscordService;
import com.gdschongik.gdsc.domain.discord.dto.response.DiscordVerificationCodeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Onboarding Discord", description = "온보딩 서비스의 디스코드 관련 API입니다.")
@RestController
@RequestMapping("/onboarding")
@RequiredArgsConstructor
public class OnboardingDiscordController {

    private final OnboardingDiscordService onboardingDiscordService;

    @Operation(summary = "디스코드 연동 인증코드 발급하기", description = "디스코드 연동을 위한 인증코드를 발급합니다.")
    @GetMapping("/discord-verification-code")
    public ResponseEntity<DiscordVerificationCodeResponse> getVerificationCode() {
        DiscordVerificationCodeResponse response = onboardingDiscordService.createVerificationCode();
        return ResponseEntity.ok().body(response);
    }
}
