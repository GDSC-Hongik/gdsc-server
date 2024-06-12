package com.gdschongik.gdsc.global.util.email;

import static com.gdschongik.gdsc.global.common.constant.EmailConstant.TOKEN_EMAIL_NAME;
import static com.gdschongik.gdsc.global.common.constant.SecurityConstant.TOKEN_ROLE_NAME;

import com.gdschongik.gdsc.domain.auth.dto.AccessTokenDto;
import com.gdschongik.gdsc.domain.auth.dto.RefreshTokenDto;
import com.gdschongik.gdsc.domain.email.dto.request.EmailVerificationTokenDto;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.global.common.constant.EmailConstant;
import com.gdschongik.gdsc.global.common.constant.JwtConstant;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
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
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VerificationTokenUtil {

    private final JwtProperty jwtProperty;

    public EmailVerificationTokenDto generateEmailVerificationToken(Long memberId, String email) {
        Date issuedAt = new Date();
        JwtProperty.TokenProperty emailVerificationTokenProperty = jwtProperty.getToken().get(JwtConstant.EMAIL_VERIFICATION_TOKEN);
        Date expiredAt = new Date(issuedAt.getTime() + emailVerificationTokenProperty.expirationMilliTime());
        Key key = getKey();

        String tokenValue = buildToken(memberId, email, issuedAt, expiredAt, key);
        return new EmailVerificationTokenDto(memberId, tokenValue, emailVerificationTokenProperty.expirationTime());
    }

    public EmailVerificationTokenDto parseEmailVerificationTokenDto(String emailVerificationTokenValue) throws ExpiredJwtException {
        try {
            Jws<Claims> claims =  Jwts.parserBuilder()
                    .requireIssuer(jwtProperty.getIssuer())
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(emailVerificationTokenValue);

            return new EmailVerificationTokenDto(
                    Long.parseLong(claims.getBody().getSubject()),
                    claims.getBody().get(TOKEN_EMAIL_NAME, String.class),
                    jwtProperty.getToken().get(JwtConstant.EMAIL_VERIFICATION_TOKEN).expirationTime());
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.EXPIRED_EMAIL_VERIFICATION_TOKEN);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_EMAIL_VERIFICATION_TOKEN);
        }
    }

    private String buildToken(Long memberId, String email, Date issuedAt, Date expiredAt, Key key) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .claim(EmailConstant.TOKEN_EMAIL_NAME, email)
                .setIssuer(jwtProperty.getIssuer())
                .setSubject(memberId.toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .signWith(key);

        return jwtBuilder.compact();
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(jwtProperty.getToken().get(JwtConstant.EMAIL_VERIFICATION_TOKEN).secret().getBytes());
    }
}
