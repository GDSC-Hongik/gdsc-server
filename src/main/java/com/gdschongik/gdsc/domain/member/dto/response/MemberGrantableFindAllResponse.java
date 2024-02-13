package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Member;

public record MemberGrantableFindAllResponse(
        Long memberId,
        String studentId,
        String name,
        String phone,
        String department,
        String email,
        String discordUsername,
        String nickname) {
    public static MemberGrantableFindAllResponse of(Member member) {
        return new MemberGrantableFindAllResponse(
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
