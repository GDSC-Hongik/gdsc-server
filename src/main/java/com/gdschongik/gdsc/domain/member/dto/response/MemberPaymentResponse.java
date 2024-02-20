package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record MemberPaymentResponse(
        @Schema(description = "회비 납부 처리에 성공한 멤버 ID 리스트") List<Long> paymentVerifiedMemberIdList) {
    public static MemberPaymentResponse from(List<Member> paymentVerifiedMembers) {
        List<Long> paymentVerifiedMemberIdList =
                paymentVerifiedMembers.stream().map(Member::getId).toList();
        return new MemberPaymentResponse(paymentVerifiedMemberIdList);
    }
}
