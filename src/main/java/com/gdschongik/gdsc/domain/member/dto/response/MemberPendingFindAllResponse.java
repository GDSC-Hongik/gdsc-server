package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.Requirement;

public record MemberPendingFindAllResponse(
        Long memberId,
        String studentId,
        String name,
        String phone,
        String department,
        String email,
        String discordUsername,
        String nickname,
        Requirement requirement) {

    public static MemberPendingFindAllResponse of(Member member) {
        return new MemberPendingFindAllResponse(
                member.getId(),
                member.getStudentId(),
                member.getName(),
                String.format(
                        "%s-%s-%s",
                        member.getPhone().substring(0, 3),
                        member.getPhone().substring(3, 7),
                        member.getPhone().substring(7)),
                member.getDepartment(),
                member.getEmail(),
                member.getDiscordUsername(),
                member.getNickname(),
                member.getRequirement());
    }
}
