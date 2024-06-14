package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.common.model.RequirementStatus;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberInfoResponse(
        Long memberId,
        String studentId,
        String name,
        String phone,
        String department,
        String email,
        String discordUsername,
        String nickname,
        @Schema(description = "디스코드 연동 상태") RequirementStatus discordStatus,
        @Schema(description = "GDSC Bevy 가입 상태") RequirementStatus bevyStatus,
        @Schema(description = "가입 상태") MemberRole role,
        @Schema(description = "입금자명") String depositorName,
        @Schema(description = "가입 상태") RegistrationStatus registrationStatus) {

    // TODO: 2차 MVP 응답 스펙에 맞게 수정 필요
    public static MemberInfoResponse of(Member member) {
        return new MemberInfoResponse(
                member.getId(),
                member.getStudentId(),
                member.getName(),
                String.format(
                        "%s-%s-%s",
                        member.getPhone().substring(0, 3),
                        member.getPhone().substring(3, 7),
                        member.getPhone().substring(7)),
                member.getDepartment().getDepartmentName(),
                member.getEmail(),
                member.getDiscordUsername(),
                member.getNickname(),
                member.getAssociateRequirement().getDiscordStatus(),
                member.getAssociateRequirement().getBevyStatus(),
                member.getRole(),
                String.format("%s%s", member.getName(), member.getPhone().substring(7)),
                RegistrationStatus.from(member));
    }

    enum RegistrationStatus {
        APPLIED,
        PENDING,
        GRANTED;

        // TODO: 2차 MVP 응답 스펙에 맞게 수정 필요
        static RegistrationStatus from(Member member) {
            return GRANTED;
        }
    }
}
