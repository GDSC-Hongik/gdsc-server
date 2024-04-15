package com.gdschongik.gdsc.domain.member.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryOption;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {
    Page<Member> findAllGrantable(MemberQueryOption queryOption, Pageable pageable);

    Page<Member> findAllByRole(MemberQueryOption queryOption, Pageable pageable, @Nullable MemberRole role);

    Page<Member> findAllByPaymentStatus(
            MemberQueryOption queryOption, RequirementStatus paymentStatus, Pageable pageable);

    Map<Boolean, List<Member>> groupByVerified(List<Long> memberIdList);

    List<Member> findAllByRole(@Nullable MemberRole role);
}
