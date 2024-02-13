package com.gdschongik.gdsc.domain.member.dto.request;

import static com.gdschongik.gdsc.global.common.constant.RegexConstant.*;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record OnboardingMemberUpdateRequest(
        @NotBlank @Schema(description = "discord username") String discordUsername,
        @NotBlank
                @Pattern(regexp = NICKNAME, message = "닉네임은 " + NICKNAME + " 형식이어야 합니다.")
                @Schema(description = "커뮤니티 닉네임", pattern = NICKNAME)
                String nickname) {}
