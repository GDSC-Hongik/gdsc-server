package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.util.formatter.PhoneFormatter;
import java.util.Optional;

public record MemberBasicInfoResponse(
        Long memberId,
        String studentId,
        String name,
        String phone,
        String department,
        String email,
        String discordUsername,
        String nickname) {
    public static MemberBasicInfoResponse from(Member member) {
        return new MemberBasicInfoResponse(
                member.getId(),
                member.getStudentId(),
                member.getName(),
                PhoneFormatter.format(member.getPhone()),
                Optional.ofNullable(member.getDepartment())
                        .map(Department::getDepartmentName)
                        .orElse(null),
                member.getEmail(),
                member.getDiscordUsername(),
                member.getNickname());
    }
}
