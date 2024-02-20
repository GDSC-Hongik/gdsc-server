package com.gdschongik.gdsc.domain.discord.dto.request;

import static com.gdschongik.gdsc.domain.discord.domain.DiscordVerificationCode.*;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record DiscordLinkRequest(
        @NotBlank @Schema(description = "디스코드 유저네임") String discordUsername,
        @Min(MIN_CODE_RANGE) @Max(MAX_CODE_RANGE) @Schema(description = "디스코드 인증코드") Integer code) {}
