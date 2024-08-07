package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.domain.study.dto.request.AssignmentCreateUpdateRequest;
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
            assertThatThrownBy(() -> studyDetailValidator.validateCancelStudyAssignment(anotherMember, studyDetail))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_DETAIL_UPDATE_RESTRICTED_TO_MENTOR.getMessage());
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
            AssignmentCreateUpdateRequest request =
                    new AssignmentCreateUpdateRequest(ASSIGNMENT_TITLE, DESCRIPTION_LINK, now.plusDays(2));

            // when & then
            assertThatThrownBy(() ->
                            studyDetailValidator.validatePublishStudyAssignment(anotherMember, studyDetail, request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_DETAIL_UPDATE_RESTRICTED_TO_MENTOR.getMessage());
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
            AssignmentCreateUpdateRequest request =
                    new AssignmentCreateUpdateRequest(ASSIGNMENT_TITLE, DESCRIPTION_LINK, now.minusDays(2));

            // when & then
            assertThatThrownBy(() -> studyDetailValidator.validatePublishStudyAssignment(mentor, studyDetail, request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ASSIGNMENT_DEADLINE_INVALID.getMessage());
        }
    }

    @Nested
    class 과제_수정시 {

        @Test
        void 멘토가_아니라면_실패한다() {
            // given
            LocalDateTime now = LocalDateTime.now();
            Member mentor = fixtureHelper.createAssociateMember(1L);
            Study study = fixtureHelper.createStudy(
                    mentor,
                    Period.createPeriod(now.plusDays(5), now.plusDays(10)),
                    Period.createPeriod(now.minusDays(5), now));

            StudyDetail studyDetail = fixtureHelper.createStudyDetail(study, now, now.plusDays(10));
            Member anotherMember = fixtureHelper.createAssociateMember(2L);
            studyDetail.publishAssignment(ASSIGNMENT_TITLE, now.plusDays(2), DESCRIPTION_LINK);

            AssignmentCreateUpdateRequest request =
                    new AssignmentCreateUpdateRequest(ASSIGNMENT_TITLE, DESCRIPTION_LINK, now.plusDays(3));

            // when & then
            assertThatThrownBy(() ->
                            studyDetailValidator.validateUpdateStudyAssignment(anotherMember, studyDetail, request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_DETAIL_UPDATE_RESTRICTED_TO_MENTOR.getMessage());
        }

        @Test
        void 기존의_마감_기한이_수정_시점보다_앞서면_실패한다() {
            // given
            Member mentor = fixtureHelper.createAssociateMember(1L);
            LocalDateTime assignmentCreatedDate = LocalDateTime.now().minusDays(1);
            Study study = fixtureHelper.createStudy(
                    mentor,
                    Period.createPeriod(assignmentCreatedDate.plusDays(5), assignmentCreatedDate.plusDays(10)),
                    Period.createPeriod(assignmentCreatedDate.minusDays(5), assignmentCreatedDate));
            StudyDetail studyDetail =
                    fixtureHelper.createStudyDetail(study, assignmentCreatedDate, assignmentCreatedDate.plusDays(1));
            studyDetail.publishAssignment(ASSIGNMENT_TITLE, assignmentCreatedDate.plusDays(1), DESCRIPTION_LINK);

            LocalDateTime assignmentUpdateDate = assignmentCreatedDate.plusDays(3);
            AssignmentCreateUpdateRequest request =
                    new AssignmentCreateUpdateRequest(ASSIGNMENT_TITLE, DESCRIPTION_LINK, assignmentUpdateDate);

            // when & then
            assertThatThrownBy(() -> studyDetailValidator.validateUpdateStudyAssignment(mentor, studyDetail, request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_DETAIL_ASSIGNMENT_INVALID_DEADLINE.getMessage());
        }

        @Test
        void 수정할_미김_기한이_기존_마감_기한_보다_앞서면_실패한다() {
            // given
            LocalDateTime now = LocalDateTime.now();
            Member mentor = fixtureHelper.createAssociateMember(1L);
            Study study = fixtureHelper.createStudy(
                    mentor,
                    Period.createPeriod(now.plusDays(5), now.plusDays(10)),
                    Period.createPeriod(now.minusDays(5), now));
            StudyDetail studyDetail = fixtureHelper.createStudyDetail(study, now, now.plusDays(10));
            LocalDateTime savedDeadLine = now.plusDays(1);
            studyDetail.publishAssignment(ASSIGNMENT_TITLE, savedDeadLine, DESCRIPTION_LINK);

            LocalDateTime updatedDeadLine = savedDeadLine.minusDays(5);
            AssignmentCreateUpdateRequest request =
                    new AssignmentCreateUpdateRequest(ASSIGNMENT_TITLE, DESCRIPTION_LINK, updatedDeadLine);

            // when & then
            assertThatThrownBy(() -> studyDetailValidator.validateUpdateStudyAssignment(mentor, studyDetail, request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_DETAIL_ASSIGNMENT_INVALID_UPDATE_DEADLINE.getMessage());
        }
    }
}
