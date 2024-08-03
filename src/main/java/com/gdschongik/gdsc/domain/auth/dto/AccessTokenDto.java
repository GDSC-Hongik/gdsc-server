package com.gdschongik.gdsc.domain.auth.dto;

import com.gdschongik.gdsc.global.security.MemberAuthInfo;

public record AccessTokenDto(MemberAuthInfo authInfo, String tokenValue) {}
