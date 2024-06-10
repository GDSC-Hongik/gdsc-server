package com.gdschongik.gdsc.domain.discord.api;

import com.gdschongik.gdsc.domain.discord.application.OnboardingDiscordService;
import com.gdschongik.gdsc.domain.discord.dto.request.DiscordLinkRequest;
import com.gdschongik.gdsc.domain.discord.dto.response.DiscordCheckDuplicateResponse;
import com.gdschongik.gdsc.domain.discord.dto.response.DiscordCheckJoinResponse;
import com.gdschongik.gdsc.domain.discord.dto.response.DiscordNicknameResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Onboarding Discord", description = "온보딩 서비스의 디스코드 관련 API입니다.")
@RestController
@RequestMapping("/onboarding")
@RequiredArgsConstructor
public class OnboardingDiscordController {

    private final OnboardingDiscordService onboardingDiscordService;

    @Operation(summary = "디스코드 연동하기", description = "디스코드 봇으로 발급받은 인증코드와 현재 사용자의 디스코드 계정을 연동합니다.")
    @PostMapping("/me/link-discord")
    public ResponseEntity<Void> linkDiscord(@Valid @RequestBody DiscordLinkRequest request) {
        onboardingDiscordService.verifyDiscordCode(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "디스코드 사용자명 중복 확인하기", description = "디스코드 사용자명이 중복되는지 확인합니다.")
    @GetMapping("/check-discord-username")
    public ResponseEntity<DiscordCheckDuplicateResponse> checkDiscordUsername(String discordUsername) {
        DiscordCheckDuplicateResponse response = onboardingDiscordService.checkUsernameDuplicate(discordUsername);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "디스코드 닉네임 중복 확인하기", description = "디스코드 닉네임이 중복되는지 확인합니다.")
    @GetMapping("/check-discord-nickname")
    public ResponseEntity<DiscordNicknameResponse> checkDiscordNickname(String nickname) {
        DiscordCheckDuplicateResponse response = onboardingDiscordService.checkNicknameDuplicate(nickname);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "디스코드 합류 확인하기", description = "해당 사용자명을 가진 유저가 디스코드 서버에 합류했는지 확인합니다.")
    @GetMapping("/check-discord-join")
    public ResponseEntity<DiscordCheckJoinResponse> checkDiscordJoin(String discordUsername) {
        DiscordCheckJoinResponse response = onboardingDiscordService.checkServerJoined(discordUsername);
        return ResponseEntity.ok(response);
    }
}
