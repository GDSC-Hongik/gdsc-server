package com.gdschongik.gdsc.domain.member.dto;

import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.domain.Member;
import java.util.Optional;

public record MemberBasicInfoDto(
        Long memberId,
        String studentId,
        String name,
        String phone,
        String department,
        String email,
        String discordUsername,
        String nickname) {
    public static MemberBasicInfoDto from(Member member) {
        return new MemberBasicInfoDto(
                member.getId(),
                member.getStudentId(),
                member.getName(),
                member.getPhone(),
                Optional.ofNullable(member.getDepartment())
                        .map(Department::getDepartmentName)
                        .orElse(null),
                member.getEmail(),
                member.getDiscordUsername(),
                member.getNickname());
    }
}
