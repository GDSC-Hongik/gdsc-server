package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.domain.Member;
import java.util.Optional;

public record MemberGrantableResponse(
        Long memberId,
        String studentId,
        String name,
        String phone,
        String department,
        String email,
        String discordUsername,
        String nickname) {

    public static MemberGrantableResponse from(Member member) {
        return new MemberGrantableResponse(
                member.getId(),
                member.getStudentId(),
                member.getName(),
                Optional.ofNullable(member.getPhone())
                        .map(phone -> String.format(
                                "%s-%s-%s", phone.substring(0, 3), phone.substring(3, 7), phone.substring(7)))
                        .orElse(null),
                Optional.ofNullable(member.getDepartment())
                        .map(Department::getDepartmentName)
                        .orElse(null),
                member.getEmail(),
                member.getDiscordUsername(),
                member.getNickname());
    }
}
