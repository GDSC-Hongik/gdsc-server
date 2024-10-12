package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.domain.member.domain.Department.D022;
import static com.gdschongik.gdsc.domain.member.domain.Member.createGuestMember;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static com.gdschongik.gdsc.global.common.constant.TemporalConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalTime;
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
            Period applicationPeriod = Period.of(START_DATE.minusDays(10), START_DATE.minusDays(5));

            // when & then
            assertThatThrownBy(() -> Study.createStudy(
                            ACADEMIC_YEAR,
                            SEMESTER_TYPE,
                            STUDY_TITLE,
                            guestMember,
                            START_TO_END_PERIOD,
                            applicationPeriod,
                            TOTAL_WEEK,
                            ONLINE_STUDY,
                            DAY_OF_WEEK,
                            STUDY_START_TIME,
                            STUDY_END_TIME))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_MENTOR_IS_UNAUTHORIZED.getMessage());
        }

        @Test
        void 신청기간_시작일이_스터디_시작일보다_늦으면_실패한다() {
            // given
            Member member = createAssociateMember(1L);
            Period period = Period.of(START_DATE, END_DATE);
            Period applicationPeriod = Period.of(START_DATE.plusDays(1), START_DATE.plusDays(2));

            // when & then
            assertThatThrownBy(() -> Study.createStudy(
                            ACADEMIC_YEAR,
                            SEMESTER_TYPE,
                            STUDY_TITLE,
                            member,
                            period,
                            applicationPeriod,
                            TOTAL_WEEK,
                            ONLINE_STUDY,
                            DAY_OF_WEEK,
                            STUDY_START_TIME,
                            STUDY_END_TIME))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_APPLICATION_START_DATE_INVALID.getMessage());
        }

        @Test
        void 온오프라인_스터디에_스터디_시각이_없으면_실패한다() {
            // given
            Member member = createAssociateMember(1L);
            Period period = Period.of(START_DATE, END_DATE);
            Period applicationPeriod = Period.of(START_DATE.minusDays(5), START_DATE.plusDays(3));

            // when & then
            assertThatThrownBy(() -> Study.createStudy(
                            ACADEMIC_YEAR,
                            SEMESTER_TYPE,
                            STUDY_TITLE,
                            member,
                            period,
                            applicationPeriod,
                            TOTAL_WEEK,
                            ONLINE_STUDY,
                            DAY_OF_WEEK,
                            null,
                            null))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ON_OFF_LINE_STUDY_TIME_IS_ESSENTIAL.getMessage());
        }

        @Test
        void 온오프라인_스터디에_스터디_시작시각이_종료시각보다_늦으면_실패한다() {
            // given
            Member member = createAssociateMember(1L);
            Period period = Period.of(START_DATE, END_DATE);
            Period applicationPeriod = Period.of(START_DATE.minusDays(5), START_DATE.plusDays(3));
            LocalTime studyStartTime = STUDY_START_TIME;
            LocalTime studyEndTime = STUDY_START_TIME.minusHours(2);

            // when & then
            assertThatThrownBy(() -> Study.createStudy(
                            ACADEMIC_YEAR,
                            SEMESTER_TYPE,
                            STUDY_TITLE,
                            member,
                            period,
                            applicationPeriod,
                            TOTAL_WEEK,
                            ONLINE_STUDY,
                            DAY_OF_WEEK,
                            studyStartTime,
                            studyEndTime))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_TIME_INVALID.getMessage());
        }

        @Test
        void 과제_스터디에_스터디_시각이_있으면_실패한다() {
            // given
            Member member = createAssociateMember(1L);
            Period period = Period.of(START_DATE, END_DATE);
            Period applicationPeriod = Period.of(START_DATE.minusDays(5), START_DATE.plusDays(3));
            LocalTime studyStartTime = STUDY_START_TIME;
            LocalTime studyEndTime = STUDY_END_TIME;

            // when & then
            assertThatThrownBy(() -> Study.createStudy(
                            ACADEMIC_YEAR,
                            SEMESTER_TYPE,
                            STUDY_TITLE,
                            member,
                            period,
                            applicationPeriod,
                            TOTAL_WEEK,
                            ASSIGNMENT_STUDY,
                            DAY_OF_WEEK,
                            studyStartTime,
                            studyEndTime))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ASSIGNMENT_STUDY_CAN_NOT_INPUT_STUDY_TIME.getMessage());
        }
    }
}
