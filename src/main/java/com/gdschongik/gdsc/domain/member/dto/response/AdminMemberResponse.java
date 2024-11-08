package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.AssociateRequirement;
import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.util.formatter.PhoneFormatter;
import java.util.Optional;

public record AdminMemberResponse(
        Long memberId,
        String studentId,
        String name,
        String phone,
        DepartmentDto department,
        String email,
        String discordUsername,
        String nickname,
        RequirementDto requirement) {

    public static AdminMemberResponse from(Member member) {
        return new AdminMemberResponse(
                member.getId(),
                member.getStudentId(),
                member.getName(),
                PhoneFormatter.format(member.getPhone()),
                DepartmentDto.from(member.getDepartment()),
                member.getEmail(),
                member.getDiscordUsername(),
                member.getNickname(),
                RequirementDto.from(member.getAssociateRequirement()));
    }

    record DepartmentDto(Department code, String name) {
        public static DepartmentDto from(Department department) {
            return Optional.ofNullable(department)
                    .map(code -> new DepartmentDto(code, code.getDepartmentName()))
                    .orElse(new DepartmentDto(null, null));
        }
    }

    record RequirementDto(String univStatus, String discordStatus) {
        public static RequirementDto from(AssociateRequirement associateRequirement) {
            return new RequirementDto(
                    associateRequirement.getUnivStatus().name(),
                    associateRequirement.getDiscordStatus().name());
        }
    }
}
