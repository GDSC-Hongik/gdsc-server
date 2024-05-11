package com.gdschongik.gdsc.domain.membership.application;

import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.integration.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

public class MembershipServiceTest extends IntegrationTest {

    @Autowired
    private MembershipService membershipService;

    @Autowired
    private MemberRepository memberRepository;

    private void setFixture() {
        Member member = Member.createGuestMember(OAUTH_ID);
        memberRepository.save(member);
    }
}
