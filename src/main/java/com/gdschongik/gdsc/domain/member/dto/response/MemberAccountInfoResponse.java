package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Member;

public record MemberAccountInfoResponse(String name, String githubHandle) {
    public static MemberAccountInfoResponse of(Member member, String githubHandle) {
        return new MemberAccountInfoResponse(member.getName(), githubHandle);
    }
}
