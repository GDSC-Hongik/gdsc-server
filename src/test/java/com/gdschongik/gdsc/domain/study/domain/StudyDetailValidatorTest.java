package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.domain.study.domain.request.AssignmentCreateRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.helper.FixtureHelper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class StudyDetailValidatorTest {

    FixtureHelper fixtureHelper = new FixtureHelper();
    StudyDetailValidator studyDetailValidator = new StudyDetailValidator();

    @Nested
    class 과제_휴강_처리시 {

        @Test
        void 멘토가_아니라면_실패한다() {
            // given
            LocalDateTime now = LocalDateTime.now();
            Member mentor = fixtureHelper.createAssociateMember(1L);
            Study study = fixtureHelper.createStudy(
                    mentor,
                    Period.createPeriod(now.plusDays(5), now.plusDays(10)),
                    Period.createPeriod(now.minusDays(5), now));
            StudyDetail studyDetail = fixtureHelper.createStudyDetail(study, now, now.plusDays(7));
            Member anotherMember = fixtureHelper.createAssociateMember(2L);

            // when & then
            assertThatThrownBy(() ->
                            studyDetailValidator.validateCancelStudyAssignment(anotherMember.getId(), studyDetail))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_DETAIL_NOT_MODIFIABLE_INVALID_ROLE.getMessage());
        }
    }

    @Nested
    class 과제_개설시 {

        @Test
        void 멘토가_아니라면_실패한다() {
            // given
            LocalDateTime now = LocalDateTime.now();
            Member mentor = fixtureHelper.createAssociateMember(1L);
            Study study = fixtureHelper.createStudy(
                    mentor,
                    Period.createPeriod(now.plusDays(5), now.plusDays(10)),
                    Period.createPeriod(now.minusDays(5), now));
            StudyDetail studyDetail = fixtureHelper.createStudyDetail(study, now, now.plusDays(7));
            Member anotherMember = fixtureHelper.createAssociateMember(2L);
            AssignmentCreateRequest request =
                    new AssignmentCreateRequest(ASSIGNMENT_TITLE, DESCRIPTION_LINK, now.plusDays(2));

            // when & then
            assertThatThrownBy(() -> studyDetailValidator.validatePublishStudyAssignment(
                            anotherMember.getId(), studyDetail, request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_DETAIL_NOT_MODIFIABLE_INVALID_ROLE.getMessage());
        }

        @Test
        void 마감_기한이_개설_시점_보다_앞서면_실패한다() {
            // given
            Member mentor = fixtureHelper.createAssociateMember(1L);
            LocalDateTime now = LocalDateTime.now();
            Study study = fixtureHelper.createStudy(
                    mentor,
                    Period.createPeriod(now.plusDays(5), now.plusDays(10)),
                    Period.createPeriod(now.minusDays(5), now));
            StudyDetail studyDetail = fixtureHelper.createStudyDetail(study, now, now.plusDays(7));
            AssignmentCreateRequest request =
                    new AssignmentCreateRequest(ASSIGNMENT_TITLE, DESCRIPTION_LINK, now.minusDays(2));

            // when & then
            assertThatThrownBy(() ->
                            studyDetailValidator.validatePublishStudyAssignment(mentor.getId(), studyDetail, request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ASSIGNMENT_DEADLINE_INVALID.getMessage());
        }
    }
}
