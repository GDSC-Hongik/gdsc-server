package com.gdschongik.gdsc.domain.membership.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    List<Membership> findAllByMember(Member member);

    Optional<Membership> findByMemberAndRecruitmentRound(Member member, RecruitmentRound recruitmentRound);
}
