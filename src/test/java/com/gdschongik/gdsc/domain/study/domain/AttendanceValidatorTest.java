package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.helper.FixtureHelper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class AttendanceValidatorTest {
    FixtureHelper fixtureHelper = new FixtureHelper();
    AttendanceValidator attendanceValidator = new AttendanceValidator();

    @Nested
    class 스터디_출석체크시 {
        @Test
        void 수강하지_않는_스터디면_실패한다() {
            // given
            Member mentor = fixtureHelper.createAssociateMember(1L);

            LocalDateTime now = LocalDateTime.now();
            Period period = Period.createPeriod(now.plusDays(10), now.plusDays(65));
            Period applicationPeriod = Period.createPeriod(now.minusDays(10), now.plusDays(5));
            Study study = fixtureHelper.createStudy(mentor, period, applicationPeriod);
            StudyDetail studyDetail = fixtureHelper.createStudyDetail(study);
            LocalDate attendanceDay = study.getPeriod()
                    .getStartDate()
                    .toLocalDate()
                    .plusDays(7
                            - study.getPeriod().getStartDate().getDayOfWeek().getValue()
                            + study.getDayOfWeek().getValue());

            // when & then
            assertThatThrownBy(() -> attendanceValidator.validateAttendance(
                            Optional.empty(), studyDetail, study, ATTENDANCE_NUMBER, attendanceDay))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_HISTORY_NOT_FOUND.getMessage());
        }

        @Test
        void 출석일자가_아니면_실패한다() {
            // given
            Member mentor = fixtureHelper.createAssociateMember(1L);

            LocalDateTime now = LocalDateTime.now();
            Period period = Period.createPeriod(now.plusDays(10), now.plusDays(65));
            Period applicationPeriod = Period.createPeriod(now.minusDays(10), now.plusDays(5));
            Study study = fixtureHelper.createStudy(mentor, period, applicationPeriod);
            StudyDetail studyDetail = fixtureHelper.createStudyDetail(study);

            Member mentee = fixtureHelper.createAssociateMember(2L);
            StudyHistory studyHistory = StudyHistory.create(mentee, study);
            LocalDate attendanceDay = study.getPeriod()
                    .getStartDate()
                    .toLocalDate()
                    .plusDays(7
                            - study.getPeriod().getStartDate().getDayOfWeek().getValue()
                            + study.getDayOfWeek().getValue());

            // when & then
            assertThatThrownBy(() -> attendanceValidator.validateAttendance(
                            Optional.of(studyHistory),
                            studyDetail,
                            study,
                            ATTENDANCE_NUMBER,
                            attendanceDay.plusDays(1)))
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
            StudyDetail studyDetail = fixtureHelper.createStudyDetail(study);

            Member mentee = fixtureHelper.createAssociateMember(2L);
            StudyHistory studyHistory = StudyHistory.create(mentee, study);
            LocalDate attendanceDay = study.getPeriod()
                    .getStartDate()
                    .toLocalDate()
                    .plusDays(7
                            - study.getPeriod().getStartDate().getDayOfWeek().getValue()
                            + study.getDayOfWeek().getValue());

            // when & then
            assertThatThrownBy(() -> attendanceValidator.validateAttendance(
                            Optional.of(studyHistory), studyDetail, study, "2345", attendanceDay))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ATTENDANCE_NUMBER_MISMATCH.getMessage());
        }
    }
}
