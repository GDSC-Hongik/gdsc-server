package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.service.StudyDetailValidator;
import com.gdschongik.gdsc.domain.study.dto.request.AssignmentCreateUpdateRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.helper.FixtureHelper;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
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
                    mentor, Period.of(now.plusDays(5), now.plusDays(10)), Period.of(now.minusDays(5), now));
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
                    mentor, Period.of(now.plusDays(5), now.plusDays(10)), Period.of(now.minusDays(5), now));
            StudyDetail studyDetail = fixtureHelper.createStudyDetail(study, now, now.plusDays(7));
            Member anotherMember = fixtureHelper.createAssociateMember(2L);
            AssignmentCreateUpdateRequest request =
                    new AssignmentCreateUpdateRequest(ASSIGNMENT_TITLE, DESCRIPTION_LINK, now.plusDays(2));

            // when & then
            assertThatThrownBy(() -> studyDetailValidator.validatePublishStudyAssignment(
                            anotherMember, studyDetail, request, now))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_DETAIL_UPDATE_RESTRICTED_TO_MENTOR.getMessage());
        }

        @Test
        void 마감_기한이_개설_시점_보다_앞서면_실패한다() {
            // given
            Member mentor = fixtureHelper.createAssociateMember(1L);
            LocalDateTime now = LocalDateTime.now();
            Study study = fixtureHelper.createStudy(
                    mentor, Period.of(now.plusDays(5), now.plusDays(10)), Period.of(now.minusDays(5), now));
            StudyDetail studyDetail = fixtureHelper.createStudyDetail(study, now, now.plusDays(7));
            AssignmentCreateUpdateRequest request =
                    new AssignmentCreateUpdateRequest(ASSIGNMENT_TITLE, DESCRIPTION_LINK, now.minusDays(2));

            // when & then
            assertThatThrownBy(() ->
                            studyDetailValidator.validatePublishStudyAssignment(mentor, studyDetail, request, now))
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
                    mentor, Period.of(now.plusDays(5), now.plusDays(10)), Period.of(now.minusDays(5), now));

            StudyDetail studyDetail = fixtureHelper.createStudyDetail(study, now, now.plusDays(10));
            Member anotherMember = fixtureHelper.createAssociateMember(2L);
            studyDetail.publishAssignment(ASSIGNMENT_TITLE, now.plusDays(2), DESCRIPTION_LINK);

            AssignmentCreateUpdateRequest request =
                    new AssignmentCreateUpdateRequest(ASSIGNMENT_TITLE, DESCRIPTION_LINK, now.plusDays(3));

            // when & then
            assertThatThrownBy(() -> studyDetailValidator.validateUpdateStudyAssignment(
                            anotherMember, studyDetail, request, now))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_DETAIL_UPDATE_RESTRICTED_TO_MENTOR.getMessage());
        }

        @Test
        void 기존마감기한이_수정시점보다_앞서면_실패한다() {
            // given
            Member mentor = fixtureHelper.createAssociateMember(1L);
            LocalDateTime assignmentCreatedDate = LocalDateTime.now().minusDays(1);
            Study study = fixtureHelper.createStudy(
                    mentor,
                    Period.of(assignmentCreatedDate.plusDays(5), assignmentCreatedDate.plusDays(10)),
                    Period.of(assignmentCreatedDate.minusDays(5), assignmentCreatedDate));
            StudyDetail studyDetail =
                    fixtureHelper.createStudyDetail(study, assignmentCreatedDate, assignmentCreatedDate.plusDays(1));
            studyDetail.publishAssignment(ASSIGNMENT_TITLE, assignmentCreatedDate.plusDays(1), DESCRIPTION_LINK);

            LocalDateTime assignmentUpdateDate = assignmentCreatedDate.plusDays(3);
            AssignmentCreateUpdateRequest request =
                    new AssignmentCreateUpdateRequest(ASSIGNMENT_TITLE, DESCRIPTION_LINK, assignmentUpdateDate);

            // when & then
            assertThatThrownBy(() -> studyDetailValidator.validateUpdateStudyAssignment(
                            mentor, studyDetail, request, LocalDateTime.now()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_DETAIL_ASSIGNMENT_INVALID_DEADLINE.getMessage());
        }

        @Test
        void 수정할_마감기한이_기존마감기한_보다_앞서면_실패한다() {
            // given
            LocalDateTime now = LocalDateTime.now();
            Member mentor = fixtureHelper.createAssociateMember(1L);
            Study study = fixtureHelper.createStudy(
                    mentor, Period.of(now.plusDays(5), now.plusDays(10)), Period.of(now.minusDays(5), now));
            StudyDetail studyDetail = fixtureHelper.createStudyDetail(study, now, now.plusDays(10));
            LocalDateTime savedDeadLine = now.minusDays(1);
            studyDetail.publishAssignment(ASSIGNMENT_TITLE, savedDeadLine, DESCRIPTION_LINK);

            LocalDateTime updatedDeadLine = savedDeadLine.minusDays(4);
            AssignmentCreateUpdateRequest request =
                    new AssignmentCreateUpdateRequest(ASSIGNMENT_TITLE, DESCRIPTION_LINK, updatedDeadLine);

            // when & then
            assertThatThrownBy(
                            () -> studyDetailValidator.validateUpdateStudyAssignment(mentor, studyDetail, request, now))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_DETAIL_ASSIGNMENT_INVALID_DEADLINE.getMessage());
        }
    }

    @Nested
    class 스터디_상세정보_작성시 {

        @Test
        void 존재하는_스터디상세정보_총개수와_요청된_스터디상세정보_총개수가_다르면_실패한다() {
            // given
            Set<Long> studyDetailIds = LongStream.rangeClosed(1, 4).boxed().collect(Collectors.toSet());

            Set<Long> requestIds = LongStream.rangeClosed(1, 5).boxed().collect(Collectors.toSet());

            // when & then
            assertThatThrownBy(() -> studyDetailValidator.validateUpdateStudyDetail(studyDetailIds, requestIds))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_DETAIL_CURRICULUM_SIZE_MISMATCH.getMessage());
        }

        @Test
        void 요청한_상세정보_id와_기존의_상세정보_id가_맞지_않으면_실패한다() {
            // given
            Set<Long> studyDetailIds = LongStream.rangeClosed(1, 4).boxed().collect(Collectors.toSet());

            Set<Long> requestIds = LongStream.rangeClosed(2, 5).boxed().collect(Collectors.toSet());

            // when & then
            assertThatThrownBy(() -> studyDetailValidator.validateUpdateStudyDetail(studyDetailIds, requestIds))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_DETAIL_ID_INVALID.getMessage());
        }
    }
}
