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

    private Long getCurrentMemberIdFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            return Long.parseLong(authentication.getName());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.AUTH_NOT_FOUND);
        }
    }

    public Long getCurrentMemberId() {
        return getCurrentMemberIdFromSecurityContext();
    }

    public Member getCurrentMember() {
        return memberRepository
                .findById(getCurrentMemberIdFromSecurityContext())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
