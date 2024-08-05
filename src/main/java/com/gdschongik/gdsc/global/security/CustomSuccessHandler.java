package com.gdschongik.gdsc.global.security;

import static com.gdschongik.gdsc.global.common.constant.SecurityConstant.*;
import static com.gdschongik.gdsc.global.common.constant.UrlConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.auth.application.JwtService;
import com.gdschongik.gdsc.domain.auth.dto.AccessTokenDto;
import com.gdschongik.gdsc.domain.auth.dto.RefreshTokenDto;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.CookieUtil;
import jakarta.servlet.http.Cookie;
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
            throws IOException {
        String baseUri = determineTargetUrl(request, response);

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // 토큰 생성 후 쿠키에 저장
        MemberAuthInfo memberAuthInfo = oAuth2User.getMemberAuthInfo();
        AccessTokenDto accessTokenDto = jwtService.createAccessToken(memberAuthInfo);
        RefreshTokenDto refreshTokenDto = jwtService.createRefreshToken(memberAuthInfo.memberId());
        cookieUtil.addTokenCookies(response, accessTokenDto.tokenValue(), refreshTokenDto.tokenValue());

        String redirectUri = UriComponentsBuilder.fromHttpUrl(baseUri)
                .path(OAUTH_REDIRECT_PATH_SEGMENT)
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        Cookie baseUriCookie = cookieUtil
                .findCookie(request, OAUTH_BASE_URI_COOKIE_NAME)
                .orElseThrow(() -> new CustomException(BASE_URI_COOKIE_NOT_FOUND));

        String baseUri = baseUriCookie.getValue();
        validateBaseUri(baseUri);

        cookieUtil.deleteCookie(response, baseUriCookie);

        return baseUri;
    }

    private void validateBaseUri(String baseUri) {
        if (!baseUri.endsWith(ROOT_DOMAIN)) {
            log.warn("허용되지 않은 BASE URI로의 리다이렉트 요청 발생. 쿠키 조작이 의심됩니다: baseUri={}", baseUri);
            throw new CustomException(NOT_ALLOWED_BASE_URI);
        }
    }
}
