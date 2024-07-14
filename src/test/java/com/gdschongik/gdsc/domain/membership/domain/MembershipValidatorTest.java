package com.gdschongik.gdsc.domain.membership.domain;

import static com.gdschongik.gdsc.domain.member.domain.Department.*;
import static com.gdschongik.gdsc.domain.member.domain.Member.*;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.common.constant.SemesterConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class MembershipValidatorTest {

    MembershipValidator membershipValidator = new MembershipValidator();

    private Member createAssociateMember(Long id) {
        Member member = createGuestMember(OAUTH_ID);
        member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
        member.completeUnivEmailVerification(UNIV_EMAIL);
        member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
        member.verifyBevy();
        member.advanceToAssociate();
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

    private RecruitmentRound createRecruitmentRound(
            Integer academicYear,
            SemesterType semesterType,
            Money fee,
            LocalDateTime startDate,
            LocalDateTime endDate) {
        Recruitment recruitment = Recruitment.createRecruitment(
                academicYear, semesterType, fee, Period.createPeriod(SEMESTER_START_DATE, SEMESTER_END_DATE));
        return RecruitmentRound.create(RECRUITMENT_NAME, startDate, endDate, recruitment, ROUND_TYPE);
    }

    @Nested
    class 멤버십_가입신청시 {
        @Test
        void 해당_리쿠르팅회차의_모집기간이_아니라면_실패한다() {
            // given
            RecruitmentRound recruitmentRound =
                    createRecruitmentRound(ACADEMIC_YEAR, SEMESTER_TYPE, FEE, START_DATE, END_DATE);

            // when & then
            assertThatThrownBy(() -> membershipValidator.validateMembershipSubmit(recruitmentRound, false))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_ROUND_NOT_OPEN.getMessage());
        }

        @Test
        void 해당_학기에_이미_Membership을_생성한_적이_있다면_실패한다() {
            // given
            Member member = createAssociateMember(1L);

            RecruitmentRound recruitmentRound =
                    createRecruitmentRound(ACADEMIC_YEAR, SEMESTER_TYPE, FEE, START_DATE, END_DATE);

            Membership membership = Membership.createMembership(member, recruitmentRound);

            // when & then
            assertThatThrownBy(() -> membershipValidator.validateMembershipSubmit(recruitmentRound, true))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(MEMBERSHIP_ALREADY_SUBMITTED.getMessage());
        }
    }
}
