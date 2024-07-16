package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.domain.member.domain.Department.D022;
import static com.gdschongik.gdsc.domain.member.domain.Member.createGuestMember;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.global.exception.CustomException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class StudyTest {

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

    @Nested
    class 스터디_개설시 {

        @Test
        void 게스트인_회원을_멘토로_지정하면_실패한다() {
            // given
            Member guestMember = Member.createGuestMember(OAUTH_ID);
            Period period = Period.createPeriod(START_DATE, END_DATE);
            Period applicationPeriod = Period.createPeriod(START_DATE.minusDays(10), START_DATE.minusDays(5));

            // when & then
            assertThatThrownBy(() -> Study.createStudy(
                            ACADEMIC_YEAR,
                            SEMESTER_TYPE,
                            guestMember,
                            period,
                            applicationPeriod,
                            TOTAL_WEEK,
                            STUDY_TYPE,
                            DAY_OF_WEEK,
                            TIME))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_MENTOR_IS_UNAUTHORIZED.getMessage());
        }

        @Test
        void 신청기간_시작일이_스터디_시작일보다_늦으면_실패한다() {
            // given
            Member member = createAssociateMember(1L);
            Period period = Period.createPeriod(START_DATE, END_DATE);
            Period applicationPeriod = Period.createPeriod(START_DATE.plusDays(1), START_DATE.plusDays(2));

            // when & then
            assertThatThrownBy(() -> Study.createStudy(
                            ACADEMIC_YEAR,
                            SEMESTER_TYPE,
                            member,
                            period,
                            applicationPeriod,
                            TOTAL_WEEK,
                            STUDY_TYPE,
                            DAY_OF_WEEK,
                            TIME))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_APPLICATION_START_DATE_INVALID.getMessage());
        }
    }
}
