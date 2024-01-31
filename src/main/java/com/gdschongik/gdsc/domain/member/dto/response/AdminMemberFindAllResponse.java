package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Member;

public record AdminMemberFindAllResponse(
        Long memberId,
        String studentId,
        String name,
        String phone,
        String department,
        String email,
        String discordUsername,
        String nickname) {

    public static AdminMemberFindAllResponse of(Member member) {
        return new AdminMemberFindAllResponse(
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
