package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.helper.FixtureHelper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class StudyHistoryValidatorTest {

    FixtureHelper fixtureHelper = new FixtureHelper();
    StudyHistoryValidator studyHistoryValidator = new StudyHistoryValidator();

    @Nested
    class 스터디_수강신청시 {

        @Test
        void 이미_해당_스터디를_신청했다면_실패한다() {
            // given
            Member mentor = fixtureHelper.createAssociateMember(1L);

            LocalDateTime now = LocalDateTime.now();
            Period period = Period.of(now.plusDays(10), now.plusDays(15));
            Period applicationPeriod = Period.of(now.minusDays(10), now.plusDays(5));
            Study study = fixtureHelper.createStudy(mentor, period, applicationPeriod);

            Member student = fixtureHelper.createGuestMember(2L);
            StudyHistory studyHistory = StudyHistory.create(student, study);

            // when & then
            assertThatThrownBy(() -> studyHistoryValidator.validateApplyStudy(study, List.of(studyHistory)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_HISTORY_DUPLICATE.getMessage());
        }

        @Test
        void 해당_스터디의_신청기간이_아니라면_실패한다() {
            // given
            Member mentor = fixtureHelper.createAssociateMember(1L);

            LocalDateTime now = LocalDateTime.now();
            Period period = Period.of(now.plusDays(10), now.plusDays(15));
            Period applicationPeriod = Period.of(now.minusDays(10), now.minusDays(5));
            Study study = fixtureHelper.createStudy(mentor, period, applicationPeriod);

            // when & then
            assertThatThrownBy(() -> studyHistoryValidator.validateApplyStudy(study, List.of()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_NOT_APPLICABLE.getMessage());
        }

        @Test
        void 이미_듣고_있는_스터디가_있다면_실패한다() {
            // given
            Member mentor = fixtureHelper.createAssociateMember(1L);

            LocalDateTime now = LocalDateTime.now();
            Period period = Period.of(now.minusDays(5), now.plusDays(15));
            Period applicationPeriod = Period.of(now.minusDays(15), now.plusDays(5));
            Study study = fixtureHelper.createStudy(mentor, period, applicationPeriod);

            Study anotherStudy = fixtureHelper.createStudy(mentor, period, applicationPeriod);

            Member student = fixtureHelper.createGuestMember(2L);
            StudyHistory studyHistory = StudyHistory.create(student, anotherStudy);

            // when & then
            assertThatThrownBy(() -> studyHistoryValidator.validateApplyStudy(study, List.of(studyHistory)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_HISTORY_ONGOING_ALREADY_EXISTS.getMessage());
        }
    }

    @Nested
    class 스터디_수강신청_취소시 {

        @Test
        void 해당_스터디의_신청기간이_아니라면_실패한다() {
            // given
            Member mentor = fixtureHelper.createAssociateMember(1L);

            LocalDateTime now = LocalDateTime.now();
            Period period = Period.of(now.plusDays(10), now.plusDays(15));
            Period applicationPeriod = Period.of(now.minusDays(10), now.minusDays(5));
            Study study = fixtureHelper.createStudy(mentor, period, applicationPeriod);

            // when & then
            assertThatThrownBy(() -> studyHistoryValidator.validateCancelStudyApply(study))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_NOT_CANCELABLE_APPLICATION_PERIOD.getMessage());
        }
    }

    @Nested
    class 레포지토리_입력시 {

        @Test
        void 이미_제출한_과제가_있다면_실패한다() {
            // given
            boolean isAnyAssignmentSubmitted = true;

            // when & then
            assertThatThrownBy(() -> studyHistoryValidator.validateUpdateRepository(
                            isAnyAssignmentSubmitted, OAUTH_ID, OAUTH_ID))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_HISTORY_REPOSITORY_NOT_UPDATABLE_ASSIGNMENT_ALREADY_SUBMITTED.getMessage());
        }

        @Test
        void 레포지토리의_소유자와_현재_멤버가_일치하지_않는다면_실패한다() {
            // given
            String wrongOauthId = "1234567";

            // when & then
            assertThatThrownBy(() -> studyHistoryValidator.validateUpdateRepository(false, wrongOauthId, OAUTH_ID))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_HISTORY_REPOSITORY_NOT_UPDATABLE_OWNER_MISMATCH.getMessage());
        }
    }

    @Nested
    class 스터디_수강신청_여부_확인시 {

        @Test
        void 해당_스터디를_신청하지_않은_멤버가_있다면_실패한다() {
            // given
            Long countStudyHistory = 1L;
            int requestStudentCount = 2;

            // when & then
            assertThatThrownBy(
                            () -> studyHistoryValidator.validateAppliedToStudy(countStudyHistory, requestStudentCount))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_HISTORY_NOT_APPLIED_STUDENT_EXISTS.getMessage());
        }
    }
}
