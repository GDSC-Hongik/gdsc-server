package com.gdschongik.gdsc.domain.auth.dto.response;

public record LoginResponse(String accessToken, String refreshToken) {

    public static LoginResponse from(String accessToken, String refreshToken) {
        return new LoginResponse(accessToken, refreshToken);
    }
}
