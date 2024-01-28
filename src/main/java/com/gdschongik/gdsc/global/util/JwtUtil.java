package com.gdschongik.gdsc.global.util;

import static com.gdschongik.gdsc.global.common.constant.SecurityConstant.*;

import com.gdschongik.gdsc.domain.auth.dto.AccessTokenDto;
import com.gdschongik.gdsc.domain.auth.dto.RefreshTokenDto;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.global.common.constant.JwtConstant;
import com.gdschongik.gdsc.global.property.JwtProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
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

    public AccessTokenDto generateAccessToken(Long memberId, MemberRole memberRole) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime()
                + jwtProperty.getToken().get(JwtConstant.ACCESS_TOKEN).expirationMilliTime());
        Key key = getKey(JwtConstant.ACCESS_TOKEN);

        String tokenValue = buildToken(memberId, memberRole, issuedAt, expiredAt, key);
        return new AccessTokenDto(memberId, memberRole, tokenValue);
    }

    public RefreshTokenDto generateRefreshToken(Long memberId) {
        Date issuedAt = new Date();
        JwtProperty.TokenProperty refreshTokenProperty = jwtProperty.getToken().get(JwtConstant.REFRESH_TOKEN);
        Date expiredAt = new Date(issuedAt.getTime() + refreshTokenProperty.expirationMilliTime());
        Key key = getKey(JwtConstant.REFRESH_TOKEN);

        String tokenValue = buildToken(memberId, null, issuedAt, expiredAt, key);
        return new RefreshTokenDto(memberId, tokenValue, refreshTokenProperty.expirationTime());
    }

    private String buildToken(Long memberId, MemberRole memberRole, Date issuedAt, Date expiredAt, Key key) {

        JwtBuilder jwtBuilder = Jwts.builder()
                .setIssuer(jwtProperty.getIssuer())
                .setSubject(memberId.toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .signWith(key);

        if (memberRole != null) {
            jwtBuilder.claim(TOKEN_ROLE_NAME, memberRole.name());
        }

        return jwtBuilder.compact();
    }

    private Key getKey(JwtConstant jwtConstant) {
        return Keys.hmacShaKeyFor(
                jwtProperty.getToken().get(jwtConstant).secret().getBytes());
    }

    public AccessTokenDto parseAccessToken(String accessTokenValue) throws ExpiredJwtException {
        try {
            Jws<Claims> claims = getClaims(JwtConstant.ACCESS_TOKEN, accessTokenValue);

            return new AccessTokenDto(
                    Long.parseLong(claims.getBody().getSubject()),
                    MemberRole.valueOf(claims.getBody().get(TOKEN_ROLE_NAME, String.class)),
                    accessTokenValue);
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (Exception e) {
            return null;
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
