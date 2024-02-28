package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.domain.Member;
import java.util.Optional;

public record MemberFindResponse(
        Long memberId,
        String studentId,
        String name,
        String phone,
        DepartmentResponse department,
        String email,
        String discordUsername,
        String nickname) {

    public static MemberFindResponse from(Member member) {
        return new MemberFindResponse(
                member.getId(),
                member.getStudentId(),
                member.getName(),
                Optional.ofNullable(member.getPhone())
                        .map(phone -> String.format(
                                "%s-%s-%s", phone.substring(0, 3), phone.substring(3, 7), phone.substring(7)))
                        .orElse(null),
                DepartmentResponse.from(member.getDepartment()),
                member.getEmail(),
                member.getDiscordUsername(),
                member.getNickname());
    }

    record DepartmentResponse(Department code, String name) {
        public static DepartmentResponse from(Department department) {
            return Optional.ofNullable(department)
                    .map(code -> new DepartmentResponse(code, code.getDepartmentName()))
                    .orElse(new DepartmentResponse(null, null));
        }
    }
}
