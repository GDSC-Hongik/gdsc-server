package com.gdschongik.gdsc.domain.member.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryRequest;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {
    Page<Member> findAll(MemberQueryRequest queryRequest, Pageable pageable);

    Optional<Member> findNormalByOauthId(String oauthId);

    Optional<Member> findVerifiedById(Long id);

    Page<Member> findAllGrantable(Pageable pageable);

    Page<Member> findAllByRole(MemberRole role, Pageable pageable);
}
