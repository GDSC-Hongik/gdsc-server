package com.gdschongik.gdsc.domain.membership.domain;

import static com.gdschongik.gdsc.domain.member.domain.MemberRole.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.dao.MembershipRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.helper.IntegrationTest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MembershipValidatorTest extends IntegrationTest {

    private MembershipValidator membershipValidator;
    private MembershipRepository membershipRepository;

    @BeforeEach
    public void setUp() {
        membershipRepository = Mockito.mock(MembershipRepository.class);
        membershipValidator = new MembershipValidator(membershipRepository);
    }

    @Nested
    class 멤버십_가입신청시 {
        @Test
        void 해당_학기에_이미_Membership을_발급받았다면_실패한다() {
            // given
            Member member = createMember();
            logoutAndReloginAs(1L, ASSOCIATE);
            RecruitmentRound recruitmentRound = createRecruitmentRound();
            Membership membership = createMembership(member, recruitmentRound);

            when(membershipRepository.findByMember(member)).thenReturn(Optional.of(membership));

            // when
            membership.verifyPaymentStatus();

            // when & then
            assertThatThrownBy(() -> membershipValidator.validateMembershipSubmit(member, recruitmentRound))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(MEMBERSHIP_ALREADY_SATISFIED.getMessage());
        }

        @Test
        void 해당_Recruitment에_대해_Membership을_생성한_적이_있다면_실패한다() {
            // given
            Member member = createMember();
            logoutAndReloginAs(1L, ASSOCIATE);
            RecruitmentRound recruitmentRound = createRecruitmentRound();
            Membership membership = createMembership(member, recruitmentRound);

            when(membershipRepository.findByMember(member)).thenReturn(Optional.of(membership));

            // when & then
            assertThatThrownBy(() -> membershipValidator.validateMembershipSubmit(member, recruitmentRound))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(MEMBERSHIP_ALREADY_SUBMITTED.getMessage());
        }

        @Test
        void 해당_RecruitmentRound의_모집기간이_아니라면_실패한다() {
            // given
            Member member = createMember();
            logoutAndReloginAs(1L, ASSOCIATE);
            RecruitmentRound recruitmentRound = createRecruitmentRound();

            // when & then
            assertThatThrownBy(() -> membershipValidator.validateMembershipSubmit(member, recruitmentRound))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_ROUND_NOT_OPEN.getMessage());
        }
    }
}
