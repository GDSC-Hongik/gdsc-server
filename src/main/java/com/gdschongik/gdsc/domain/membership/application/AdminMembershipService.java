package com.gdschongik.gdsc.domain.membership.application;

import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.domain.membership.dao.MembershipRepository;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.membership.domain.dto.request.MembershipQueryOption;
import com.gdschongik.gdsc.domain.membership.domain.dto.response.MembershipResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMembershipService {
    private final MembershipRepository membershipRepository;
    public Page<MembershipResponse> getMembershipByPaymentStatus(
            MembershipQueryOption queryOption, RequirementStatus paymentStatus, Pageable pageable) {
        Page<Membership> applications =
                membershipRepository.findAllByPaymentStatus(queryOption, paymentStatus, pageable);
        return applications.map(MembershipResponse::from);
    }
}
