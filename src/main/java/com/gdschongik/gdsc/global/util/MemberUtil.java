package com.gdschongik.gdsc.global.util;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberUtil {

    private final MemberRepository memberRepository;

    public Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        validateAuthenticationNotNull(authentication);

        try {
            return Long.parseLong(authentication.getName());
        } catch (NumberFormatException e) {
            throw new CustomException(ErrorCode.AUTH_NOT_PARSABLE);
        }
    }

    private void validateAuthenticationNotNull(Authentication authentication) {
        if (authentication == null) {
            throw new CustomException(ErrorCode.AUTH_NOT_EXIST);
        }
    }

    public Member getCurrentMember() {
        return memberRepository
                .findById(getCurrentMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
