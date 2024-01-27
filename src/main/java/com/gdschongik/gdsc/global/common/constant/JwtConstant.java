package com.gdschongik.gdsc.global.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtConstant {
    ACCESS_TOKEN(Constants.ACCESS_TOKEN_COOKIE_NAME),
    REFRESH_TOKEN(Constants.REFRESH_TOKEN_COOKIE_NAME);

    private final String cookieName;

    private static class Constants {
        public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
        public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    }
}
