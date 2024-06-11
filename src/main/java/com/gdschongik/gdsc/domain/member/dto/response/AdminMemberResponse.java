package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.AssociateRequirement;
import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.domain.Member;
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
                Optional.ofNullable(member.getPhone())
                        .map(phone -> String.format(
                                "%s-%s-%s", phone.substring(0, 3), phone.substring(3, 7), phone.substring(7)))
                        .orElse(null),
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

    record RequirementDto(String univStatus, String discordStatus, String bevyStatus) {
        public static RequirementDto from(AssociateRequirement associateRequirement) {
            return new RequirementDto(
                    associateRequirement.getUnivStatus().name(),
                    associateRequirement.getDiscordStatus().name(),
                    associateRequirement.getBevyStatus().name());
        }
    }
}
