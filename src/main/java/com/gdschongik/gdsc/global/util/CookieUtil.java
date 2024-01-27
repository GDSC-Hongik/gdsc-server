package com.gdschongik.gdsc.global.util;

import static com.gdschongik.gdsc.global.common.constant.SecurityConstant.*;

import com.gdschongik.gdsc.domain.auth.domain.TokenType;
import com.gdschongik.gdsc.global.property.JwtProperty;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieUtil {

    private final JwtProperty jwtProperty;

    public void addTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        HttpHeaders headers = generateTokenCookies(accessToken, refreshToken);
        headers.forEach((key, value) -> response.addHeader(key, value.get(0)));
    }

    private HttpHeaders generateTokenCookies(String accessToken, String refreshToken) {
        // TODO: Prod profile일 때는 Strict, 아니면 None으로 설정
        String sameSite = "None";

        ResponseCookie accessTokenCookie = generateCookie(
                ACCESS_TOKEN_COOKIE_NAME,
                accessToken,
                jwtProperty.getToken().get(TokenType.ACCESS_TOKEN).expirationTime(),
                sameSite);

        ResponseCookie refreshTokenCookie = generateCookie(
                REFRESH_TOKEN_COOKIE_NAME,
                refreshToken,
                jwtProperty.getToken().get(TokenType.REFRESH_TOKEN).expirationTime(),
                sameSite);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return headers;
    }

    private ResponseCookie generateCookie(String cookieName, String tokenValue, Long expiration, String sameSite) {
        return ResponseCookie.from(cookieName, tokenValue)
                .path("/")
                .maxAge(expiration)
                .secure(true)
                .sameSite(sameSite)
                .httpOnly(false)
                .build();
    }
}
