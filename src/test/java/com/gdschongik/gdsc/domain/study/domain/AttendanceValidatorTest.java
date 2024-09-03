package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.common.constant.StudyConstant.ATTENDANCE_NUMBER;
import static com.gdschongik.gdsc.global.exception.ErrorCode.ALREADY_ATTENDED_STUDY_DETAIL;
import static com.gdschongik.gdsc.global.exception.ErrorCode.ATTENDANCE_DATE_INVALID;
import static com.gdschongik.gdsc.global.exception.ErrorCode.ATTENDANCE_NUMBER_MISMATCH;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.helper.FixtureHelper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class AttendanceValidatorTest {
    FixtureHelper fixtureHelper = new FixtureHelper();
    AttendanceValidator attendanceValidator = new AttendanceValidator();

    @Nested
    class 스터디_출석체크시 {

        @Test
        void 출석일자가_아니면_실패한다() {
            // given
            Member mentor = fixtureHelper.createAssociateMember(1L);

            LocalDateTime now = LocalDateTime.now();
            Period period = Period.createPeriod(now.plusDays(10), now.plusDays(65));
            Period applicationPeriod = Period.createPeriod(now.minusDays(10), now.plusDays(5));
            Study study = fixtureHelper.createStudy(mentor, period, applicationPeriod);
            StudyDetail studyDetail = fixtureHelper.createStudyDetail(study, now, now.plusDays(7));

            LocalDate attendanceDay = studyDetail.getAttendanceDay();

            // when & then
            assertThatThrownBy(() -> attendanceValidator.validateAttendance(
                            studyDetail, ATTENDANCE_NUMBER, attendanceDay.plusDays(1), false))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ATTENDANCE_DATE_INVALID.getMessage());
        }

        @Test
        void 출석번호가_다르면_실패한다() {
            // given
            Member mentor = fixtureHelper.createAssociateMember(1L);

            LocalDateTime now = LocalDateTime.now();
            Period period = Period.createPeriod(now.plusDays(10), now.plusDays(65));
            Period applicationPeriod = Period.createPeriod(now.minusDays(10), now.plusDays(5));
            Study study = fixtureHelper.createStudy(mentor, period, applicationPeriod);
            StudyDetail studyDetail = fixtureHelper.createStudyDetail(study, now, now.plusDays(7));

            LocalDate attendanceDay = studyDetail.getAttendanceDay();

            // when & then
            assertThatThrownBy(() -> attendanceValidator.validateAttendance(
                            studyDetail, ATTENDANCE_NUMBER + 1, attendanceDay, false))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ATTENDANCE_NUMBER_MISMATCH.getMessage());
        }

        @Test
        void 출석을_이미_진행했으면_실패한다() {
            // given
            Member mentor = fixtureHelper.createAssociateMember(1L);

            LocalDateTime now = LocalDateTime.now();
            Period period = Period.createPeriod(now.plusDays(10), now.plusDays(65));
            Period applicationPeriod = Period.createPeriod(now.minusDays(10), now.plusDays(5));
            Study study = fixtureHelper.createStudy(mentor, period, applicationPeriod);
            StudyDetail studyDetail = fixtureHelper.createStudyDetail(study, now, now.plusDays(7));

            LocalDate attendanceDay = studyDetail.getAttendanceDay();

            // when & then
            assertThatThrownBy(() ->
                            attendanceValidator.validateAttendance(studyDetail, ATTENDANCE_NUMBER, attendanceDay, true))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ALREADY_ATTENDED_STUDY_DETAIL.getMessage());
        }
    }
}
