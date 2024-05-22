package com.gdschongik.gdsc.domain.membership.dao;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    boolean existsByMemberAndAcademicYearAndSemesterType(
            Member member, Integer academicYear, SemesterType semesterType);
}
