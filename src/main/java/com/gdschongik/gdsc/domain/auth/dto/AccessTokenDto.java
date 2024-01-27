package com.gdschongik.gdsc.domain.auth.dto;

import com.gdschongik.gdsc.domain.member.domain.MemberRole;

public record AccessTokenDto(
	Long memberId,
	MemberRole memberRole,
	String tokenValue
) {
}
