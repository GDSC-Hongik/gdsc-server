package com.gdschongik.gdsc.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberTokenByOauthIdRequest(@NotBlank String oauthId) {}
