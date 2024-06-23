package com.gdschongik.gdsc.domain.membership.application;

import static com.gdschongik.gdsc.domain.common.model.RequirementStatus.SATISFIED;
import static com.gdschongik.gdsc.domain.member.domain.MemberRole.ASSOCIATE;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.dao.MembershipRepository;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.integration.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MembershipServiceTest extends IntegrationTest {

    @Autowired
    private MembershipService membershipService;

    @Autowired
    private MembershipRepository membershipRepository;

    private Membership createMembership(Member member, Recruitment recruitment) {
        Membership membership = Membership.createMembership(member, recruitment);
        return membershipRepository.save(membership);
    }

    @Nested
    class 멤버십_가입신청시 {
        @Test
        void Recruitment가_없다면_실패한다() {
            // given
            createMember();
            logoutAndReloginAs(1L, ASSOCIATE);
            Long recruitmentId = 1L;

            // when & then
            assertThatThrownBy(() -> membershipService.submitMembership(recruitmentId))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_NOT_FOUND.getMessage());
        }

        @Test
        void 해당_학기에_이미_Membership을_발급받았다면_실패한다() {
            // given
            Member member = createMember();
            logoutAndReloginAs(1L, ASSOCIATE);
            Recruitment recruitment = createRecruitment();
            Membership membership = createMembership(member, recruitment);

            // when
            membership.verifyPaymentStatus();
            membershipRepository.save(membership);

            // then
            assertThatThrownBy(() -> membershipService.submitMembership(recruitment.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(MEMBERSHIP_ALREADY_SATISFIED.getMessage());
        }

        @Test
        void 해당_Recruitment에_대해_Membership을_생성한_적이_있다면_실패한다() {
            // given
            Member member = createMember();
            logoutAndReloginAs(1L, ASSOCIATE);
            Recruitment recruitment = createRecruitment();
            createMembership(member, recruitment);

            // when & then
            assertThatThrownBy(() -> membershipService.submitMembership(recruitment.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(MEMBERSHIP_ALREADY_APPLIED.getMessage());
        }

        @Test
        void 해당_Recruitment의_모집기간이_아니라면_실패한다() {
            // given
            createMember();
            logoutAndReloginAs(1L, ASSOCIATE);
            Recruitment recruitment = createRecruitment();

            // when & then
            assertThatThrownBy(() -> membershipService.submitMembership(recruitment.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_NOT_OPEN.getMessage());
        }
    }

    @Test
    void 멤버십_회비납부시_이미_회비납부_했다면_회비납부_실패한다() {
        // given
        Member member = createMember();
        logoutAndReloginAs(1L, ASSOCIATE);
        Recruitment recruitment = createRecruitment();
        Membership membership = createMembership(member, recruitment);
        membershipService.verifyPaymentStatus(membership.getId());

        // when & then
        assertThatThrownBy(() -> membershipService.verifyPaymentStatus(membership.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(MEMBERSHIP_ALREADY_SATISFIED.getMessage());
    }

    @Nested
    class 정회원_가입조건_인증시도시 {
        @Test
        void 멤버십_회비납부시_정회원_가입조건중_회비납부_인증상태가_인증_성공한다() {
            // given
            Member member = createMember();
            logoutAndReloginAs(1L, ASSOCIATE);
            Recruitment recruitment = createRecruitment();
            Membership membership = createMembership(member, recruitment);

            // when
            membershipService.verifyPaymentStatus(membership.getId());
            membership = membershipRepository.findById(membership.getId()).get();

            // then
            assertThat(membership.getRegularRequirement().getPaymentStatus()).isEqualTo(SATISFIED);
        }
    }
}
