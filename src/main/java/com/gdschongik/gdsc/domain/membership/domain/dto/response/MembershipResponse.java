package com.gdschongik.gdsc.domain.membership.domain.dto.response;

import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.domain.membership.domain.Membership;

public record MembershipResponse(Long memberShipId, Long memberId, RequirementStatus status) {
    public static MembershipResponse from(Membership membership) {
        return new MembershipResponse(
                membership.getId(), membership.getMember().getId(), membership.getPaymentStatus());
    }
}
