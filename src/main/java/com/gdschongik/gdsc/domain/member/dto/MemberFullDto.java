package com.gdschongik.gdsc.domain.member.dto;

import com.gdschongik.gdsc.domain.member.domain.AssociateRequirement;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;

public record MemberFullDto(
        Long memberId, MemberRole role, MemberBasicInfoDto basicInfo, AssociateRequirement associateRequirement) {
    public static MemberFullDto from(Member member) {
        return new MemberFullDto(
                member.getId(), member.getRole(), MemberBasicInfoDto.from(member), member.getAssociateRequirement());
    }

    record MemberBasicInfoDto(
            String name,
            String studentId,
            String email,
            String department,
            String phone,
            String discordUsername,
            String nickname) {
        public static MemberBasicInfoDto from(Member member) {
            return new MemberBasicInfoDto(
                    member.getName(),
                    member.getStudentId(),
                    member.getEmail(),
                    member.getDepartment().getDepartmentName(),
                    member.getPhone(),
                    member.getDiscordUsername(),
                    member.getNickname());
        }
    }
}
