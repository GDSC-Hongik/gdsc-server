package com.gdschongik.gdsc.domain.auth.api;

import static com.gdschongik.gdsc.global.common.constant.JwtConstant.Constants.*;

import com.gdschongik.gdsc.global.util.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 API입니다.")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CookieUtil cookieUtil;

    @Operation(summary = "로그아웃", description = "현재 엑세스 토큰 및 리프레시 토큰 쿠키를 만료시킵니다.")
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(ACCESS_TOKEN_COOKIE_NAME) Cookie accessToken,
            @CookieValue(REFRESH_TOKEN_COOKIE_NAME) Cookie refreshToken) {
        cookieUtil.deleteCookie(accessToken);
        cookieUtil.deleteCookie(refreshToken);
        return ResponseEntity.ok().build();
    }
}
