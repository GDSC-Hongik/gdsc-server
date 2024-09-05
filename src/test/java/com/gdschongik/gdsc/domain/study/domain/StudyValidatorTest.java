package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.helper.FixtureHelper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class StudyValidatorTest {

    FixtureHelper fixtureHelper = new FixtureHelper();
    StudyValidator studyValidator = new StudyValidator();

    // FixtureHelper 래핑 메서드
    private Member createMentor(Long id) {
        return fixtureHelper.createMentor(id);
    }

    private Member createMember(Long id) {
        return fixtureHelper.createAssociateMember(id);
    }

    private Member createAdmin(Long id) {
        return fixtureHelper.createAdmin(id);
    }

    @Nested
    class 스터디_멘토역할_검증시 {

        @Test
        void 멘토역할이_아니라면_실패한다() {
            // given
            Member currentMember = createMember(1L);
            Member mentor = createMentor(2L);
            LocalDateTime assignmentCreatedDate = LocalDateTime.now().minusDays(1);
            Study study = fixtureHelper.createStudy(
                    mentor,
                    Period.createPeriod(assignmentCreatedDate.plusDays(5), assignmentCreatedDate.plusDays(10)),
                    Period.createPeriod(assignmentCreatedDate.minusDays(5), assignmentCreatedDate));

            // when & then
            assertThatThrownBy(() -> studyValidator.validateStudyMentor(currentMember, study))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_ACCESS_NOT_ALLOWED.getMessage());
        }

        @Test
        void 멘토이지만_자신이_맡은_스터디가_아니라면_실패한다() {
            // given
            Member currentMember = createMentor(1L);
            Member mentor = createMentor(2L);
            LocalDateTime assignmentCreatedDate = LocalDateTime.now().minusDays(1);
            Study study = fixtureHelper.createStudy(
                    mentor,
                    Period.createPeriod(assignmentCreatedDate.plusDays(5), assignmentCreatedDate.plusDays(10)),
                    Period.createPeriod(assignmentCreatedDate.minusDays(5), assignmentCreatedDate));

            Study currentMentorStudy = fixtureHelper.createStudy(
                    currentMember,
                    Period.createPeriod(assignmentCreatedDate.plusDays(5), assignmentCreatedDate.plusDays(10)),
                    Period.createPeriod(assignmentCreatedDate.minusDays(5), assignmentCreatedDate));

            // when & then
            assertThat(currentMentorStudy.getMentor().getId()).isEqualTo(currentMember.getId());
            assertThatThrownBy(() -> studyValidator.validateStudyMentor(currentMember, study))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_MENTOR_INVALID.getMessage());
        }

        @Test
        void 어드민이라면_성공한다() {
            // given
            Member admin = createAdmin(1L);
            Member mentor = createMentor(2L);
            LocalDateTime assignmentCreatedDate = LocalDateTime.now().minusDays(1);
            Study study = fixtureHelper.createStudy(
                    mentor,
                    Period.createPeriod(assignmentCreatedDate.plusDays(5), assignmentCreatedDate.plusDays(10)),
                    Period.createPeriod(assignmentCreatedDate.minusDays(5), assignmentCreatedDate));

            // when & then
            assertThatCode(() -> studyValidator.validateStudyMentor(admin, study))
                    .doesNotThrowAnyException();
        }
    }
}
