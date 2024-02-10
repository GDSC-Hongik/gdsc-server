package com.gdschongik.gdsc.global.security;

import static com.gdschongik.gdsc.global.common.constant.SecurityConstant.*;

import com.gdschongik.gdsc.domain.auth.application.JwtService;
import com.gdschongik.gdsc.domain.auth.dto.AccessTokenDto;
import com.gdschongik.gdsc.domain.auth.dto.RefreshTokenDto;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.global.common.constant.JwtConstant;
import com.gdschongik.gdsc.global.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CookieUtil cookieUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessTokenHeaderValue = extractAccessTokenFromHeader(request);
        String accessTokenValue = extractTokenValue(JwtConstant.ACCESS_TOKEN, request);
        String refreshTokenValue = extractTokenValue(JwtConstant.REFRESH_TOKEN, request);

        // 헤더에 AT가 있으면 우선적으로 검증 (Swagger 테스트 전용)
        if (accessTokenHeaderValue != null) {
            AccessTokenDto accessTokenDto = jwtService.retrieveAccessToken(accessTokenHeaderValue);
            if (accessTokenDto != null) {
                setAuthenticationToContext(accessTokenDto.memberId(), accessTokenDto.memberRole());
                filterChain.doFilter(request, response);
                return;
            }
        }

        // AT와 RT 중 하나라도 없으면 실패
        if (accessTokenValue == null || refreshTokenValue == null) {
            filterChain.doFilter(request, response);
            return;
        }

        AccessTokenDto accessTokenDto = jwtService.retrieveAccessToken(accessTokenValue);

        // AT가 유효하면 통과
        if (accessTokenDto != null) {
            setAuthenticationToContext(accessTokenDto.memberId(), accessTokenDto.memberRole());
            filterChain.doFilter(request, response);
            return;
        }

        Optional<AccessTokenDto> reissueAccessToken =
                Optional.ofNullable(jwtService.reissueAccessTokenIfExpired(accessTokenValue));
        RefreshTokenDto refreshTokenDto = jwtService.retrieveRefreshToken(refreshTokenValue);

        // AT가 만료되었고, RT가 유효하면 AT, RT 재발급
        if (reissueAccessToken.isPresent() && refreshTokenDto != null) {
            AccessTokenDto accessToken = reissueAccessToken.get();
            RefreshTokenDto refreshToken = jwtService.createRefreshToken(refreshTokenDto.memberId());
            cookieUtil.addTokenCookies(response, accessToken.tokenValue(), refreshToken.tokenValue());
            setAuthenticationToContext(accessToken.memberId(), accessToken.memberRole());
        }

        // AT, RT 둘 다 만료되었으면 실패
        filterChain.doFilter(request, response);
    }

    private String extractTokenValue(JwtConstant jwtConstant, HttpServletRequest request) {
        return Optional.ofNullable(WebUtils.getCookie(request, jwtConstant.getCookieName()))
                .map(Cookie::getValue)
                .orElse(null);
    }

    private String extractAccessTokenFromHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith(TOKEN_HEADER_PREFIX))
                .map(header -> header.replace(TOKEN_HEADER_PREFIX, ""))
                .orElse(null);
    }

    private void setAuthenticationToContext(Long memberId, MemberRole memberRole) {
        UserDetails userDetails = new PrincipalDetails(memberId, memberRole);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
