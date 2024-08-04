package com.gdschongik.gdsc.global.security;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberManageRole;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.domain.MemberStudyRole;

/**
 * 엑세스 토큰 및 시큐리티 내부 로직에서 사용되는 회원 정보 DTO입니다.
 */
public record MemberAuthInfo(Long memberId, MemberRole role, MemberManageRole manageRole, MemberStudyRole studyRole) {
    public static MemberAuthInfo from(Member member) {
        return new MemberAuthInfo(member.getId(), member.getRole(), member.getManageRole(), member.getStudyRole());
    }
}
