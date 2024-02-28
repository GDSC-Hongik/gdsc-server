package com.gdschongik.gdsc.global.security;

import com.gdschongik.gdsc.global.common.constant.SecurityConstant;
import com.gdschongik.gdsc.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class CustomAuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final int COOKIE_EXPIRE_SECONDS = 180;

    private final CookieUtil cookieUtil;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return cookieUtil
                .getCookie(request, SecurityConstant.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> cookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(
            OAuth2AuthorizationRequest authRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authRequest == null) return;

        cookieUtil.addCookie(
                response,
                SecurityConstant.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                cookieUtil.serialize(authRequest),
                COOKIE_EXPIRE_SECONDS);

        String redirectUriAfterLogin = request.getParameter(SecurityConstant.REDIRECT_URI);
        if (StringUtils.hasText(redirectUriAfterLogin))
            cookieUtil.addCookie(response, SecurityConstant.REDIRECT_URI, redirectUriAfterLogin, COOKIE_EXPIRE_SECONDS);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(
            HttpServletRequest request, HttpServletResponse response) {
        cookieUtil.deleteCookie(request, response, SecurityConstant.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        cookieUtil.deleteCookie(request, response, SecurityConstant.REDIRECT_URI);
        return null;
    }
}
