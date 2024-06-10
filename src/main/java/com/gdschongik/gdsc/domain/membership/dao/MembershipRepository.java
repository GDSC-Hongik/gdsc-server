package com.gdschongik.gdsc.domain.membership.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long>, MembershipCustomRepository {

    boolean existsByMemberAndRecruitment(Member member, Recruitment recruitment);
}
