package com.gdschongik.gdsc.domain.membership.dao;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.member.domain.Member;

public interface MembershipCustomRepository {
    boolean existsIssuedMembership(Member member, Integer academicYear, SemesterType semesterType);
}
