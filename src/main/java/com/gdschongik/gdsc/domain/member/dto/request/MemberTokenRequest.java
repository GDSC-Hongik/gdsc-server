package com.gdschongik.gdsc.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberTokenRequest(@NotBlank String oauth_id) {}
