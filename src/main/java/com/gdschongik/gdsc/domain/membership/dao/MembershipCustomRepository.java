package com.gdschongik.gdsc.domain.membership.dao;

import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.membership.domain.dto.request.MembershipQueryOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MembershipCustomRepository {
    Page<Membership> findAllByPaymentStatus(
            MembershipQueryOption queryOption, RequirementStatus paymentStatus, Pageable pageable);
}
