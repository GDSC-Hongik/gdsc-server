package com.gdschongik.gdsc.domain.auth.dto;

public record RefreshTokenDto(
	Long memberId,
	String tokenValue,
	Long ttl
) {
}
