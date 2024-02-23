package com.gdschongik.gdsc.domain.member.dao;

import java.util.Optional;

import com.gdschongik.gdsc.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    boolean existsByDiscordUsername(String discordUsername);

    boolean existsByNickname(String nickname);

	Optional<Member> findByDiscordUsername(String discordUsername);
}
