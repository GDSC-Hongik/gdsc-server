package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.dto.MemberFullDto;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.membership.dto.MembershipFullDto;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.dto.RecruitmentFullDto;

public record MemberDashboardResponse(
        MemberFullDto member, RecruitmentFullDto currentRecruitment, MembershipFullDto currentMembership) {
    public static MemberDashboardResponse from(
            Member member, Recruitment currentRecruitment, Membership currentMembership) {
        return new MemberDashboardResponse(
                MemberFullDto.from(member),
                RecruitmentFullDto.from(currentRecruitment),
                MembershipFullDto.from(currentMembership));
    }
}
