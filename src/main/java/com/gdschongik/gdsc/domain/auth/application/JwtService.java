package com.gdschongik.gdsc.domain.auth.application;

import com.gdschongik.gdsc.domain.auth.dao.RefreshTokenRepository;
import com.gdschongik.gdsc.domain.auth.domain.RefreshToken;
import com.gdschongik.gdsc.domain.auth.dto.AccessTokenDto;
import com.gdschongik.gdsc.domain.auth.dto.RefreshTokenDto;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.global.util.JwtUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public AccessTokenDto createAccessToken(Long memberId, MemberRole memberRole) {
        return jwtUtil.generateAccessToken(memberId, memberRole);
    }

    public RefreshTokenDto createRefreshToken(Long memberId) {
        RefreshTokenDto refreshTokenDto = jwtUtil.generateRefreshToken(memberId);
        saveRefreshTokenToRedis(refreshTokenDto);
        return refreshTokenDto;
    }

    private void saveRefreshTokenToRedis(RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken = RefreshToken.builder()
                .memberId(refreshTokenDto.memberId())
                .token(refreshTokenDto.tokenValue())
                .ttl(refreshTokenDto.ttl())
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    public AccessTokenDto retrieveAccessToken(String accessTokenValue) {
        try {
            return jwtUtil.parseAccessToken(accessTokenValue);
        } catch (Exception e) {
            log.error("엑세스 토큰 파싱에 실패했습니다: {}", accessTokenValue);
            return null;
        }
    }

    public RefreshTokenDto retrieveRefreshToken(String refreshTokenValue) {
        RefreshTokenDto refreshTokenDto = parseRefreshToken(refreshTokenValue);

        // 파싱된 DTO와 일치하는 토큰이 Redis에 저장되어 있는지 확인
        Optional<RefreshToken> refreshToken = getRefreshTokenFromRedis(refreshTokenDto.memberId());

        // Redis에 토큰이 존재하고, 쿠키의 토큰과 값이 일치하면 DTO 반환
        if (refreshToken.isPresent()
                && refreshTokenDto.tokenValue().equals(refreshToken.get().getToken())) {
            return refreshTokenDto;
        }

        // Redis에 토큰이 존재하지 않거나, 쿠키의 토큰과 값이 일치하지 않으면 null 반환
        return null;
    }

    private Optional<RefreshToken> getRefreshTokenFromRedis(Long memberId) {
        // TODO: CustomException으로 바꾸기
        return refreshTokenRepository.findByMemberId(memberId);
    }

    private RefreshTokenDto parseRefreshToken(String refreshTokenValue) {
        try {
            return jwtUtil.parseRefreshToken(refreshTokenValue);
        } catch (Exception e) {
            log.error("리프레시 토큰 파싱에 실패했습니다: {}", refreshTokenValue);
            return null;
        }
    }
}
