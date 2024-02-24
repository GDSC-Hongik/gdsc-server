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
        String phone = null;
        if (member.getPhone() != null) {
            phone = String.format(
                    "%s-%s-%s",
                    member.getPhone().substring(0, 3),
                    member.getPhone().substring(3, 7),
                    member.getPhone().substring(7));
        }
        return new MemberFindAllResponse(
                member.getId(),
                member.getStudentId(),
                member.getName(),
                phone,
                member.getDepartment().getDepartmentName(),
                member.getEmail(),
                member.getDiscordUsername(),
                member.getNickname());
    }
}
