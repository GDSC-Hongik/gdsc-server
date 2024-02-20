package com.gdschongik.gdsc.domain.discord.dto.request;

import static com.gdschongik.gdsc.domain.discord.domain.DiscordVerificationCode.*;
import static com.gdschongik.gdsc.global.common.constant.RegexConstant.*;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DiscordLinkRequest(
        @NotBlank @Schema(description = "디스코드 유저네임") String discordUsername,
        @NotBlank
                @Pattern(regexp = NICKNAME, message = "닉네임은 " + NICKNAME + " 형식이어야 합니다.")
                @Schema(description = "커뮤니티 닉네임", pattern = NICKNAME)
                String nickname,
        @Min(MIN_CODE_RANGE) @Max(MAX_CODE_RANGE) @Schema(description = "디스코드 인증코드") Integer code) {}
