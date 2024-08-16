package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.STUDY_MENTOR_INVALID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Nested
    class 스터디_수강원_명단_조회시 {

        @Test
        public void 멘토가_아니라면_실패한다() {
            // given
            Member currentMember = fixtureHelper.createAssociateMember(1L);
            Member mentor = fixtureHelper.createAssociateMember(2L);
            LocalDateTime assignmentCreatedDate = LocalDateTime.now().minusDays(1);
            Study study = fixtureHelper.createStudy(
                    mentor,
                    Period.createPeriod(assignmentCreatedDate.plusDays(5), assignmentCreatedDate.plusDays(10)),
                    Period.createPeriod(assignmentCreatedDate.minusDays(5), assignmentCreatedDate));

            // when & then
            assertThatThrownBy(() -> studyValidator.validateStudyMentor(
                            currentMember, study.getMentor().getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_MENTOR_INVALID.getMessage());
        }
    }
}
