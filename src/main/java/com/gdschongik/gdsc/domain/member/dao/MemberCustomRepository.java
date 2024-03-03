package com.gdschongik.gdsc.domain.member.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryRequest;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {
    Optional<Member> findNormalByOauthId(String oauthId);

    Page<Member> findAllGrantable(MemberQueryRequest queryRequest, Pageable pageable);

    Page<Member> findAllByRole(MemberQueryRequest queryRequest, Pageable pageable, @Nullable MemberRole role);

    Page<Member> findAllByPaymentStatus(
            MemberQueryRequest queryRequest, RequirementStatus paymentStatus, Pageable pageable);

    Map<Boolean, List<Member>> groupByVerified(List<Long> memberIdList);
}
