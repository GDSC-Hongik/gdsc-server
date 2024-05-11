package com.gdschongik.gdsc.domain.membership.application;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.dao.MembershipRepository;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final MemberUtil memberUtil;

    @Transactional
    public void applyMembership() {
        Member currentMember = memberUtil.getCurrentMember();
        Membership membership = Membership.createMembership(currentMember);
        membershipRepository.save(membership);
    }
}
