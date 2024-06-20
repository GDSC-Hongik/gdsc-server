package com.gdschongik.gdsc.domain.membership.dto;

import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.membership.domain.RegularRequirement;

public record MembershipFullDto(
        Long membershipId, Long memberId, Long recruitmentId, RegularRequirement regularRequirement) {
    public static MembershipFullDto from(Membership membership) {
        return new MembershipFullDto(
                membership.getId(),
                membership.getMember().getId(),
                membership.getRecruitment().getId(),
                membership.getRegularRequirement());
    }
}
