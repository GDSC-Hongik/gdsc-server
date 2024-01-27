package com.gdschongik.gdsc.global.util;

import static com.gdschongik.gdsc.global.common.constant.SecurityConstant.*;

import com.gdschongik.gdsc.domain.auth.domain.TokenType;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.global.property.JwtProperty;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperty jwtProperty;

    public String generateAccessToken(Long memberId, MemberRole memberRole) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime()
                + jwtProperty.getToken().get(TokenType.ACCESS_TOKEN).expirationMilliTime());
        Key key = getKey(TokenType.ACCESS_TOKEN);
        return buildToken(memberId, memberRole, issuedAt, expiredAt, key);
    }

    public String generateRefreshToken(Long memberId) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime()
                + jwtProperty.getToken().get(TokenType.REFRESH_TOKEN).expirationMilliTime());
        Key key = getKey(TokenType.REFRESH_TOKEN);
        return buildToken(memberId, null, issuedAt, expiredAt, key);
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

    private Key getKey(TokenType tokenType) {
        return Keys.hmacShaKeyFor(jwtProperty.getToken().get(tokenType).secret().getBytes());
    }
}
