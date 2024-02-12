package com.gdschongik.gdsc.global.security;

import static com.gdschongik.gdsc.global.common.constant.SecurityConstant.*;

import com.gdschongik.gdsc.domain.auth.application.JwtService;
import com.gdschongik.gdsc.domain.auth.dto.AccessTokenDto;
import com.gdschongik.gdsc.domain.auth.dto.RefreshTokenDto;
import com.gdschongik.gdsc.global.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final CookieUtil cookieUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // 랜딩 페이지 결정에 필요한 정보를 헤더에 추가
        response.setHeader(LANDING_STATUS_HEADER, oAuth2User.getLandingStatus().name());

        // 토큰 생성 후 쿠키에 저장
        AccessTokenDto accessTokenDto =
                jwtService.createAccessToken(oAuth2User.getMemberId(), oAuth2User.getMemberRole());
        RefreshTokenDto refreshTokenDto = jwtService.createRefreshToken(oAuth2User.getMemberId());
        cookieUtil.addTokenCookies(response, accessTokenDto.tokenValue(), refreshTokenDto.tokenValue());
    }
}
