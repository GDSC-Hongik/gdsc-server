package com.gdschongik.gdsc.domain.member.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {
    Page<Member> findAll(MemberQueryRequest queryRequest, Pageable pageable);

    Optional<Member> findNormalByOauthId(String oauthId);

    Page<Member> findAllGrantable(Pageable pageable);

    Page<Member> findAllByRole(MemberRole role, Pageable pageable);

    Page<Member> findAllByPaymentStatus(RequirementStatus paymentStatus, Pageable pageable);

    Map<Boolean, List<Member>> groupByVerified(List<Long> memberIdList);
}
