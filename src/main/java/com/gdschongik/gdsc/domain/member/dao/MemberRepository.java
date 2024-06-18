package com.gdschongik.gdsc.domain.member.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    boolean existsByDiscordUsername(String discordUsername);

    boolean existsByNickname(String nickname);

    Optional<Member> findByDiscordUsername(String discordUsername);

    Optional<Member> findByUnivEmail(String univEmail);

    Optional<Member> findByOauthId(String oauthId);
}
