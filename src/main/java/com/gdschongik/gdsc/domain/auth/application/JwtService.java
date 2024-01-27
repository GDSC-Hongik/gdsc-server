package com.gdschongik.gdsc.domain.auth.application;

import com.gdschongik.gdsc.domain.auth.dao.RefreshTokenRepository;
import com.gdschongik.gdsc.domain.auth.domain.RefreshToken;
import com.gdschongik.gdsc.domain.auth.dto.AccessTokenDto;
import com.gdschongik.gdsc.domain.auth.dto.RefreshTokenDto;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.global.util.JwtUtil;
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
}
