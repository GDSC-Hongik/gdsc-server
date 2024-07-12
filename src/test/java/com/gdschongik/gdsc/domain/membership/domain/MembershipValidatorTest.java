package com.gdschongik.gdsc.domain.membership.domain;

import static com.gdschongik.gdsc.domain.member.domain.Department.*;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.common.constant.SemesterConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MembershipValidatorTest {

    MembershipValidator membershipValidator = new MembershipValidator();

    @Nested
    class 멤버십_가입신청시 {
        @Test
        void 해당_RecruitmentRound의_모집기간이_아니라면_실패한다() {
            // given
            Recruitment recruitment = Recruitment.createRecruitment(
                    ACADEMIC_YEAR, SEMESTER_TYPE, FEE, Period.createPeriod(SEMESTER_START_DATE, SEMESTER_END_DATE));
            RecruitmentRound recruitmentRound =
                    RecruitmentRound.create(RECRUITMENT_NAME, START_DATE, END_DATE, recruitment, ROUND_TYPE);

            // when & then
            assertThatThrownBy(() -> membershipValidator.validateMembershipSubmit(recruitmentRound, Optional.empty()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_ROUND_NOT_OPEN.getMessage());
        }

        @Test
        void 해당_학기에_이미_Membership을_발급받았다면_실패한다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);
            member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
            member.verifyBevy();
            member.advanceToAssociate();

            Recruitment recruitment = Recruitment.createRecruitment(
                    ACADEMIC_YEAR, SEMESTER_TYPE, FEE, Period.createPeriod(SEMESTER_START_DATE, SEMESTER_END_DATE));

            RecruitmentRound recruitmentRound =
                    RecruitmentRound.create(RECRUITMENT_NAME, START_DATE, END_DATE, recruitment, ROUND_TYPE);

            Membership membership = Membership.createMembership(member, recruitmentRound);

            // when
            membership.verifyPaymentStatus();

            // then
            assertThatThrownBy(() ->
                            membershipValidator.validateMembershipSubmit(recruitmentRound, Optional.of(membership)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(MEMBERSHIP_ALREADY_SATISFIED.getMessage());
        }

        @Test
        void 해당_학기에_이미_Membership을_생성한_적이_있다면_실패한다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);
            member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
            member.verifyBevy();
            member.advanceToAssociate();

            Recruitment recruitment = Recruitment.createRecruitment(
                    ACADEMIC_YEAR, SEMESTER_TYPE, FEE, Period.createPeriod(SEMESTER_START_DATE, SEMESTER_END_DATE));

            RecruitmentRound recruitmentRound =
                    RecruitmentRound.create(RECRUITMENT_NAME, START_DATE, END_DATE, recruitment, ROUND_TYPE);

            Membership membership = Membership.createMembership(member, recruitmentRound);

            // when & then
            assertThatThrownBy(() ->
                            membershipValidator.validateMembershipSubmit(recruitmentRound, Optional.of(membership)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(MEMBERSHIP_ALREADY_SUBMITTED.getMessage());
        }
    }
}
