package com.gdschongik.gdsc.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberTokenResponse(
        @Schema(description = "액세스 토큰") String accessToken, @Schema(description = "리프레쉬 토큰") String refreshToken) {}
