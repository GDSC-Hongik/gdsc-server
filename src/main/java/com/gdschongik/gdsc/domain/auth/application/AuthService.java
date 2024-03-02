package com.gdschongik.gdsc.domain.auth.application;

import com.gdschongik.gdsc.domain.auth.dto.request.LoginRequest;
import com.gdschongik.gdsc.domain.auth.dto.response.LoginResponse;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.property.SwaggerProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final SwaggerProperty swaggerProperty;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    public LoginResponse loginAdmin(LoginRequest request) {
        Member member = memberRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getRole() != MemberRole.ADMIN) {
            log.error("Invalid role");
            throw new CustomException(ErrorCode.INVALID_ROLE);
        }

        if (!request.password().equals(swaggerProperty.getPassword())) {
            log.error("Invalid password");
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken =
                jwtService.createAccessToken(member.getId(), member.getRole()).tokenValue();
        String refreshToken = jwtService.createRefreshToken(member.getId()).tokenValue();

        return LoginResponse.from(accessToken, refreshToken);
    }
}
