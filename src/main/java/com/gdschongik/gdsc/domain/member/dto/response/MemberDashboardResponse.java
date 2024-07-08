package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.dto.MemberFullDto;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.membership.dto.MembershipFullDto;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.domain.recruitment.dto.RecruitmentRoundFullDto;
import jakarta.annotation.Nullable;

public record MemberDashboardResponse(
        MemberFullDto member,
        RecruitmentRoundFullDto currentRecruitmentRound,
        @Nullable MembershipFullDto currentMembership) {
    public static MemberDashboardResponse from(
            Member member, RecruitmentRound currentRecruitmentRound, Membership currentMembership) {
        return new MemberDashboardResponse(
                MemberFullDto.from(member),
                RecruitmentRoundFullDto.from(currentRecruitmentRound),
                currentMembership == null ? null : MembershipFullDto.from(currentMembership));
    }
}
