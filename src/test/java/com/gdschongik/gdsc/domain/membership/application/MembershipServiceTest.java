package com.gdschongik.gdsc.domain.membership.application;

import static com.gdschongik.gdsc.domain.common.model.RequirementStatus.SATISFIED;
import static com.gdschongik.gdsc.domain.member.domain.MemberRole.ASSOCIATE;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.dao.MembershipRepository;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.helper.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MembershipServiceTest extends IntegrationTest {

    @Autowired
    private MembershipService membershipService;

    @Autowired
    private MembershipRepository membershipRepository;

    @Nested
    class 멤버십_가입신청시 {
        @Test
        void RecruitmentRound가_없다면_실패한다() {
            // given
            createMember();
            logoutAndReloginAs(1L, ASSOCIATE);
            Long recruitmentRoundId = 1L;

            // when & then
            assertThatThrownBy(() -> membershipService.submitMembership(recruitmentRoundId))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_ROUND_NOT_FOUND.getMessage());
        }

        // todo: 1차 모집시 멤버십 생성 후 실제 가입은 하지 않고 2차 모집 시 가입하려고 하는 케이스 추가
    }

    @Test
    void 멤버십_회비납부시_이미_회비납부_했다면_회비납부_실패한다() {
        // given
        Member member = createMember();
        logoutAndReloginAs(1L, ASSOCIATE);
        RecruitmentRound recruitmentRound = createRecruitmentRound();
        Membership membership = createMembership(member, recruitmentRound);
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
            RecruitmentRound recruitmentRound = createRecruitmentRound();
            Membership membership = createMembership(member, recruitmentRound);

            // when
            membershipService.verifyPaymentStatus(membership.getId());
            membership = membershipRepository.findById(membership.getId()).get();

            // then
            assertThat(membership.getRegularRequirement().getPaymentStatus()).isEqualTo(SATISFIED);
        }
    }
}
