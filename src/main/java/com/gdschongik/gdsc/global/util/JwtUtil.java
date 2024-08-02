package com.gdschongik.gdsc.global.util;

import static com.gdschongik.gdsc.global.common.constant.SecurityConstant.*;

import com.gdschongik.gdsc.domain.auth.dto.AccessTokenDto;
import com.gdschongik.gdsc.domain.auth.dto.RefreshTokenDto;
import com.gdschongik.gdsc.domain.member.domain.MemberManageRole;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.domain.MemberStudyRole;
import com.gdschongik.gdsc.global.common.constant.JwtConstant;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.property.JwtProperty;
import com.gdschongik.gdsc.global.security.MemberAuthInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * JWT 토큰을 생성하고 파싱하는 유틸 클래스<br>
 * 토큰 저장, 재발급 등의 기능은 {@link com.gdschongik.gdsc.domain.auth.application.JwtService}에서 담당한다.<br>
 * JWT 토큰을 사용하기 위해서는 해당 클래스를 사용해야 하며, JwtUtil을 직접 사용하지 않도록 주의한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperty jwtProperty;

    public AccessTokenDto generateAccessToken(MemberAuthInfo authInfo) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime()
                + jwtProperty.getToken().get(JwtConstant.ACCESS_TOKEN).expirationMilliTime());
        Key key = getKey(JwtConstant.ACCESS_TOKEN);

        String tokenValue = buildAccessToken(authInfo, issuedAt, expiredAt, key);
        return new AccessTokenDto(authInfo, tokenValue);
    }

    private String buildAccessToken(MemberAuthInfo authInfo, Date issuedAt, Date expiredAt, Key key) {
        return Jwts.builder()
                .setIssuer(jwtProperty.getIssuer())
                .setSubject(authInfo.memberId().toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .claim(TOKEN_ROLE_NAME, authInfo.role().name())
                .claim(TOKEN_MANAGE_ROLE_NAME, authInfo.manageRole().name())
                .claim(TOKEN_STUDY_ROLE_NAME, authInfo.studyRole().name())
                .signWith(key)
                .compact();
    }

    public RefreshTokenDto generateRefreshToken(Long memberId) {
        Date issuedAt = new Date();
        JwtProperty.TokenProperty refreshTokenProperty = jwtProperty.getToken().get(JwtConstant.REFRESH_TOKEN);
        Date expiredAt = new Date(issuedAt.getTime() + refreshTokenProperty.expirationMilliTime());
        Key key = getKey(JwtConstant.REFRESH_TOKEN);

        String tokenValue = buildRefreshToken(memberId, issuedAt, expiredAt, key);
        return new RefreshTokenDto(memberId, tokenValue, refreshTokenProperty.expirationTime());
    }

    private String buildRefreshToken(Long memberId, Date issuedAt, Date expiredAt, Key key) {
        return Jwts.builder()
                .setIssuer(jwtProperty.getIssuer())
                .setSubject(memberId.toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .signWith(key)
                .compact();
    }

    private Key getKey(JwtConstant jwtConstant) {
        return Keys.hmacShaKeyFor(
                jwtProperty.getToken().get(jwtConstant).secret().getBytes());
    }

    public AccessTokenDto parseAccessToken(String accessTokenValue) throws ExpiredJwtException {
        try {
            Jws<Claims> claims = getClaims(JwtConstant.ACCESS_TOKEN, accessTokenValue);

            MemberAuthInfo parsedAuthInfo = new MemberAuthInfo(
                    Long.parseLong(claims.getBody().getSubject()),
                    MemberRole.valueOf(claims.getBody().get(TOKEN_ROLE_NAME, String.class)),
                    MemberManageRole.valueOf(claims.getBody().get(TOKEN_MANAGE_ROLE_NAME, String.class)),
                    MemberStudyRole.valueOf(claims.getBody().get(TOKEN_STUDY_ROLE_NAME, String.class)));

            return new AccessTokenDto(parsedAuthInfo, accessTokenValue);
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
        }
    }

    public RefreshTokenDto parseRefreshToken(String refreshTokenValue) throws ExpiredJwtException {
        try {
            Jws<Claims> claims = getClaims(JwtConstant.REFRESH_TOKEN, refreshTokenValue);

            return new RefreshTokenDto(
                    Long.parseLong(claims.getBody().getSubject()),
                    refreshTokenValue,
                    jwtProperty.getToken().get(JwtConstant.REFRESH_TOKEN).expirationTime());
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (Exception e) {
            return null;
        }
    }

    private Jws<Claims> getClaims(JwtConstant tokenType, String tokenValue) {
        Key key = getKey(tokenType);
        return Jwts.parserBuilder()
                .requireIssuer(jwtProperty.getIssuer())
                .setSigningKey(key)
                .build()
                .parseClaimsJws(tokenValue);
    }
}
