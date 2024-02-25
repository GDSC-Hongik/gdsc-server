package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.Requirement;
import java.util.Optional;

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
        String phone = null;
        if (member.getPhone() != null) {
            phone = String.format(
                    "%s-%s-%s",
                    member.getPhone().substring(0, 3),
                    member.getPhone().substring(3, 7),
                    member.getPhone().substring(7));
        }
        return new MemberPendingFindAllResponse(
                member.getId(),
                member.getStudentId(),
                member.getName(),
                phone,
                Optional.ofNullable(member.getDepartment())
                        .map(Department::getDepartmentName)
                        .orElse(null),
                member.getEmail(),
                member.getDiscordUsername(),
                member.getNickname(),
                member.getRequirement());
    }
}
