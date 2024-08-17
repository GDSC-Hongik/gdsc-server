package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.helper.FixtureHelper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class StudyAssignmentHistoryValidatorTest {

    private final FixtureHelper fixtureHelper = new FixtureHelper();
    private final StudyAssignmentHistoryValidator validator = new StudyAssignmentHistoryValidator();

    // FixtureHelper 래핑 메서드
    private Member createMember(Long id) {
        return fixtureHelper.createAssociateMember(id);
    }

    private Study createStudyWithMentor(Long mentorId) {
        Period period = Period.createPeriod(STUDY_START_DATETIME, STUDY_END_DATETIME);
        Period applicationPeriod =
                Period.createPeriod(STUDY_START_DATETIME.minusDays(7), STUDY_START_DATETIME.minusDays(1));
        return fixtureHelper.createStudyWithMentor(mentorId, period, applicationPeriod);
    }

    private StudyDetail createStudyDetailWithAssignment(Study study) {
        return fixtureHelper.createStudyDetailWithAssignment(
                study, STUDY_DETAIL_START_DATETIME, STUDY_DETAIL_END_DATETIME, STUDY_ASSIGNMENT_DEADLINE_DATETIME);
    }

    @Nested
    class 과제_제출가능_여부_검증할때 {

        @Test
        void 스터디_수강신청_기록이_없다면_실패한다() {
            // given
            Study study = createStudyWithMentor(1L);
            StudyDetail studyDetail = createStudyDetailWithAssignment(study);
            boolean isAppliedToStudy = false;

            // when & then
            assertThatThrownBy(() -> validator.validateSubmitAvailable(
                            isAppliedToStudy, STUDY_DETAIL_START_DATETIME, studyDetail))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ASSIGNMENT_STUDY_NOT_APPLIED.getMessage());
        }

        @Test
        void 과제가_시작되지_않았다면_실패한다() {
            // given
            Study study = createStudyWithMentor(1L);
            StudyDetail studyDetail = createStudyDetailWithAssignment(study);
            boolean isAppliedToStudy = true;
            LocalDateTime beforeStart = STUDY_DETAIL_START_DATETIME.minusDays(1);

            // when & then
            assertThatThrownBy(() -> validator.validateSubmitAvailable(isAppliedToStudy, beforeStart, studyDetail))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ASSIGNMENT_SUBMIT_NOT_STARTED.getMessage());
        }

        @Test
        void 과제_마감기한이_지났다면_실패한다() {
            // given
            Study study = createStudyWithMentor(1L);
            StudyDetail studyDetail = createStudyDetailWithAssignment(study);
            boolean isAppliedToStudy = true;
            LocalDateTime afterDeadline = STUDY_ASSIGNMENT_DEADLINE_DATETIME.plusDays(1);

            // when & then
            assertThatThrownBy(() -> validator.validateSubmitAvailable(isAppliedToStudy, afterDeadline, studyDetail))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ASSIGNMENT_SUBMIT_DEADLINE_PASSED.getMessage());
        }

        @Test
        void 모든_조건을_만족하는_경우_성공한다() {
            // given
            Study study = createStudyWithMentor(1L);
            StudyDetail studyDetail = createStudyDetailWithAssignment(study);
            boolean isAppliedToStudy = true;

            // when & then
            assertThatCode(() -> validator.validateSubmitAvailable(
                            isAppliedToStudy, STUDY_DETAIL_START_DATETIME, studyDetail))
                    .doesNotThrowAnyException();
        }
    }
}
