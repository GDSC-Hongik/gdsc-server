package com.gdschongik.gdsc.domain.member.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByOauthId(String oauthId);
}
