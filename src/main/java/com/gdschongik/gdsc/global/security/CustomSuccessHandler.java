package com.gdschongik.gdsc.global.security;

import static com.gdschongik.gdsc.global.common.constant.SecurityConstant.*;
import static com.gdschongik.gdsc.global.common.constant.UrlConstant.*;

import com.gdschongik.gdsc.domain.auth.application.JwtService;
import com.gdschongik.gdsc.domain.auth.dto.AccessTokenDto;
import com.gdschongik.gdsc.domain.auth.dto.RefreshTokenDto;
import com.gdschongik.gdsc.global.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final CookieUtil cookieUtil;

    public CustomSuccessHandler(JwtService jwtService, CookieUtil cookieUtil) {
        this.jwtService = jwtService;
        this.cookieUtil = cookieUtil;
        this.setUseReferer(true);
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // 토큰 생성 후 쿠키에 저장
        AccessTokenDto accessTokenDto =
                jwtService.createAccessToken(oAuth2User.getMemberId(), oAuth2User.getMemberRole());
        RefreshTokenDto refreshTokenDto = jwtService.createRefreshToken(oAuth2User.getMemberId());
        cookieUtil.addTokenCookies(response, accessTokenDto.tokenValue(), refreshTokenDto.tokenValue());

        // 임시로 헤더에 엑세스 토큰 추가
        response.addHeader(ACCESS_TOKEN_HEADER_NAME, ACCESS_TOKEN_HEADER_PREFIX + accessTokenDto.tokenValue());

        // 랜딩 상태를 파라미터로 추가하여 리다이렉트
        // String baseUrl = determineTargetUrl(request, response);
        String baseUrl = PROD_CLIENT_URL + "/";
        String redirectUrl = String.format(
                SOCIAL_LOGIN_REDIRECT_URL,
                baseUrl,
                LANDING_STATUS_PARAM,
                oAuth2User.getLandingStatus().name(),
                "access",
                accessTokenDto.tokenValue(),
                "refresh",
                refreshTokenDto.tokenValue());

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
