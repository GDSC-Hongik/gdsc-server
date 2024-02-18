package com.gdschongik.gdsc.domain.member.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    Page<Member> findAllByRole(MemberRole role, Pageable pageable);
}
