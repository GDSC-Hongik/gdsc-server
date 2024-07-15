package com.gdschongik.gdsc.domain.membership.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;

public interface MembershipCustomRepository {

    boolean existsByMemberAndRecruitment(Member member, Recruitment recruitment);
}
