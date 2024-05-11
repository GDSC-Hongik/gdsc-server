package com.gdschongik.gdsc.domain.membership.dao;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.membership.domain.dto.request.MembershipQueryOption;
import com.gdschongik.gdsc.repository.RepositoryTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static com.gdschongik.gdsc.global.common.constant.MemberConstant.OAUTH_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class AdminMembershipRepositoryTest extends RepositoryTest {
    private MembershipQueryOption EMPTY_QUERY_OPTION = new MembershipQueryOption(null, null);

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Membership setFixture() {
        Member member = Member.createGuestMember(OAUTH_ID);
        memberRepository.save(member);
        Membership membership = Membership.createMembership(member);
        return membershipRepository.save(membership);
    }

    @Nested
    class 가입신청_조회할때 {
        @Test
        void 회비납부_안했으면_PENDING으로_조회_성공() {
            // given
            Membership membership = setFixture();

            // when
            Page<Membership> memberships = membershipRepository.findAllByPaymentStatus(
                    EMPTY_QUERY_OPTION, RequirementStatus.PENDING, PageRequest.of(0, 10));

            // then
            assertThat(memberships).contains(membership);
        }

        @Test
        void 회비납부_안헀으면_VERIFIED로_조회되지_않는다() {
            // given
            Membership membership = setFixture();

            // when
            Page<Membership> memberships = membershipRepository.findAllByPaymentStatus(
                    EMPTY_QUERY_OPTION, RequirementStatus.VERIFIED, PageRequest.of(0, 10));

            // then
            assertThat(memberships).doesNotContain(membership);
        }
    }
}
