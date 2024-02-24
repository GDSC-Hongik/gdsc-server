package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;

public record MemberPaymentFindAllResponse(
        Long memberId,
        String studentId,
        String name,
        String phone,
        String discordUsername,
        String nickname,
        RequirementStatus paymentStatus) {
    public static MemberPaymentFindAllResponse from(Member member) {
        return new MemberPaymentFindAllResponse(
                member.getId(),
                member.getStudentId(),
                member.getName(),
                member.getPhone(),
                member.getDiscordUsername(),
                member.getNickname(),
                member.getRequirement().getPaymentStatus());
    }
}
