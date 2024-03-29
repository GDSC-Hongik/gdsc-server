package com.gdschongik.gdsc.global.util;

import com.gdschongik.gdsc.global.common.constant.JwtConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieUtil {

    private final EnvironmentUtil environmentUtil;

    public void addTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {

        String sameSite = determineSameSitePolicy();

        ResponseCookie accessTokenCookie =
                generateTokenCookie(JwtConstant.ACCESS_TOKEN.getCookieName(), accessToken, sameSite);

        ResponseCookie refreshTokenCookie =
                generateTokenCookie(JwtConstant.REFRESH_TOKEN.getCookieName(), refreshToken, sameSite);

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    private ResponseCookie generateTokenCookie(String cookieName, String tokenValue, String sameSite) {
        return ResponseCookie.from(cookieName, tokenValue)
                .path("/")
                .secure(true)
                .sameSite(sameSite)
                .httpOnly(true)
                .build();
    }

    private String determineSameSitePolicy() {
        if (environmentUtil.isProdProfile()) {
            return Cookie.SameSite.LAX.attributeValue();
        }
        return Cookie.SameSite.NONE.attributeValue();
    }

    public Optional<jakarta.servlet.http.Cookie> findCookie(HttpServletRequest request, String cookieName) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst();
    }

    public void deleteCookie(HttpServletResponse response, jakarta.servlet.http.Cookie cookie) {
        cookie.setPath("/");
        cookie.setValue("");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
