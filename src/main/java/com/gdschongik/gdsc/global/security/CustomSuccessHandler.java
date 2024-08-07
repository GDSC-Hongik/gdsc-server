package com.gdschongik.gdsc.global.security;

import static com.gdschongik.gdsc.global.common.constant.SecurityConstant.*;

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
        setUseReferer(true);
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // 토큰 생성 후 쿠키에 저장
        MemberAuthInfo memberAuthInfo = oAuth2User.getMemberAuthInfo();
        AccessTokenDto accessTokenDto = jwtService.createAccessToken(memberAuthInfo);
        RefreshTokenDto refreshTokenDto = jwtService.createRefreshToken(memberAuthInfo.memberId());
        cookieUtil.addTokenCookies(response, accessTokenDto.tokenValue(), refreshTokenDto.tokenValue());

        super.onAuthenticationSuccess(request, response, authentication);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        String baseUrl = super.determineTargetUrl(request, response);
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(OAUTH_REDIRECT_PATH_SEGMENT)
                .build()
                .toUriString();
    }
}
