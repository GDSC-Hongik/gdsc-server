package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
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
        @Schema(description = "회비 입금 상태") RequirementStatus paymentStatus,
        @Schema(description = "가입 상태") MemberRole role) {

    public static MemberInfoResponse of(Member member) {
        return new MemberInfoResponse(
                member.getId(),
                member.getStudentId(),
                member.getName(),
                member.getPhone(),
                member.getDepartment(),
                member.getEmail(),
                member.getDiscordUsername(),
                member.getNickname(),
                member.getRequirement().getPaymentStatus(),
                member.getRole());
    }
}
