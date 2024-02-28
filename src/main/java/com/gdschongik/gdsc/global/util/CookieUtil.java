package com.gdschongik.gdsc.global.util;

import com.gdschongik.gdsc.global.common.constant.JwtConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

@Component
@RequiredArgsConstructor
public class CookieUtil {

    private final EnvironmentUtil environmentUtil;

    public void addTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {

        String sameSite = determineSameSitePolicy();

        ResponseCookie accessTokenCookie =
                generateCookie(JwtConstant.ACCESS_TOKEN.getCookieName(), accessToken, sameSite);

        ResponseCookie refreshTokenCookie =
                generateCookie(JwtConstant.REFRESH_TOKEN.getCookieName(), refreshToken, sameSite);

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    private ResponseCookie generateCookie(String cookieName, String tokenValue, String sameSite) {
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

    public Optional<jakarta.servlet.http.Cookie> getCookie(HttpServletRequest request, String name) {
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) return Optional.empty();

        return Arrays.stream(cookies)
                .filter(cookie -> StringUtils.equals(cookie.getName(), name))
                .findAny();
    }

    public void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie(name, value);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie(name, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public String serialize(Object object) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
    }

    public <T> T deserialize(jakarta.servlet.http.Cookie cookie, Class<T> clazz) {
        return clazz.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}
