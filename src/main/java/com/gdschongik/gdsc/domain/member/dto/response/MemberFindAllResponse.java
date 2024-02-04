package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Member;

public record MemberFindAllResponse(
        Long memberId,
        String studentId,
        String name,
        String phone,
        String department,
        String email,
        String discordUsername,
        String nickname) {

    public static MemberFindAllResponse of(Member member) {
        return new MemberFindAllResponse(
                member.getId(),
                member.getStudentId(),
                member.getName(),
                member.getPhone(),
                member.getDepartment(),
                member.getEmail(),
                member.getDiscordUsername(),
                member.getNickname());
    }
}
