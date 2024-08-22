package com.gdschongik.gdsc.domain.member.dto.response;

import static com.gdschongik.gdsc.global.common.constant.GithubConstant.*;

import com.gdschongik.gdsc.domain.member.domain.Member;

public record MemberAccountInfoResponse(String name, String githubHandle, String githubLink) {
    public static MemberAccountInfoResponse of(Member member, String githubHandle) {
        return new MemberAccountInfoResponse(
                member.getName(), githubHandle, GITHUB_PROFILE_URL.formatted(githubHandle));
    }
}
