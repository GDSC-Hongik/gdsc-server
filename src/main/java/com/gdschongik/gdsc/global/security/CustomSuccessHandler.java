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
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final CookieUtil cookieUtil;

    public CustomSuccessHandler(JwtService jwtService, CookieUtil cookieUtil) {
        this.jwtService = jwtService;
        this.cookieUtil = cookieUtil;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {
        String baseUri = determineTargetUrl(request, response);

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // 토큰 생성 후 쿠키에 저장
        AccessTokenDto accessTokenDto =
                jwtService.createAccessToken(oAuth2User.getMemberId(), oAuth2User.getMemberRole());
        RefreshTokenDto refreshTokenDto = jwtService.createRefreshToken(oAuth2User.getMemberId());
        cookieUtil.addTokenCookies(response, accessTokenDto.tokenValue(), refreshTokenDto.tokenValue());

        // 임시로 헤더에 엑세스 토큰 추가
        response.addHeader(ACCESS_TOKEN_HEADER_NAME, ACCESS_TOKEN_HEADER_PREFIX + accessTokenDto.tokenValue());

        // 랜딩 상태를 파라미터로 추가하여 리다이렉트
        String baseUrl = oAuth2User.getRedirectUri();
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
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        Cookie baseUriCookie = cookieUtil
            .findCookie(request, OAUTH_BASE_URI_COOKIE_NAME)
            .orElseThrow(() -> new CustomException(ErrorCode.BASE_URI_COOKIE_NOT_FOUND));

        String baseUri = baseUriCookie.getValue();
        validateBaseUri(baseUri);

        cookieUtil.deleteCookie(baseUriCookie);
        return baseUri;
    }

    private void validateBaseUri(String baseUri) {
        if (baseUri.endsWith(ROOT_DOMAIN) || LOCAL_CLIENT_URLS.contains(baseUri)) {
            return;
        }

        log.error("허용되지 않은 BASE URI로의 리다이렉트 요청 발생: {}", baseUri);
        throw new CustomException(ErrorCode.NOT_ALLOWED_BASE_URI);
    }
}
