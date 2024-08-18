package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.domain.study.dto.request.AssignmentCreateUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.request.StudySessionCreateRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.helper.FixtureHelper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class StudyDetailValidatorTest {

    FixtureHelper fixtureHelper = new FixtureHelper();
    StudyDetailValidator studyDetailValidator = new StudyDetailValidator();

    private StudyDetail createNewStudyDetail(Long week, Study study, LocalDateTime now, LocalDateTime plusDays) {
        return fixtureHelper.createNewStudyDetail(study, week, now, plusDays);
    }

    private List<StudySessionCreateRequest> createSessionCreateRequest(int count) {
        List<StudySessionCreateRequest> requests = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            requests.add(new StudySessionCreateRequest(
                    (long) i, "title " + i, "설명 " + i, Difficulty.HIGH, StudyStatus.OPEN));
        }
        return requests;
    }

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
        void 기존마감기한이_수정시점보다_앞서면_실패한다() {
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
        void 수정할_마감기한이_기존마감기한_보다_앞서면_실패한다() {
            // given
            LocalDateTime now = LocalDateTime.now();
            Member mentor = fixtureHelper.createAssociateMember(1L);
            Study study = fixtureHelper.createStudy(
                    mentor,
                    Period.createPeriod(now.plusDays(5), now.plusDays(10)),
                    Period.createPeriod(now.minusDays(5), now));
            StudyDetail studyDetail = fixtureHelper.createStudyDetail(study, now, now.plusDays(10));
            LocalDateTime savedDeadLine = now.minusDays(1);
            studyDetail.publishAssignment(ASSIGNMENT_TITLE, savedDeadLine, DESCRIPTION_LINK);

            LocalDateTime updatedDeadLine = savedDeadLine.minusDays(4);
            AssignmentCreateUpdateRequest request =
                    new AssignmentCreateUpdateRequest(ASSIGNMENT_TITLE, DESCRIPTION_LINK, updatedDeadLine);

            // when & then
            assertThatThrownBy(() -> studyDetailValidator.validateUpdateStudyAssignment(mentor, studyDetail, request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_DETAIL_ASSIGNMENT_INVALID_DEADLINE.getMessage());
        }
    }

    @Nested
    class 스터디_상세정보_작성시 {

        @Test
        void 존재하는_스터디상세정보_총개수와_요청된_스터디상세정보_총개수가_다르면_실패한다() {
            // given
            LocalDateTime now = LocalDateTime.now();
            Member mentor = fixtureHelper.createMentor(1L);
            Study study = fixtureHelper.createStudy(
                    mentor,
                    Period.createPeriod(now.plusDays(5), now.plusDays(10)),
                    Period.createPeriod(now.minusDays(5), now));

            List<StudyDetail> studyDetails = new ArrayList<>();
            for (int i = 1; i <= 4; i++) {
                Long week = (long) i;
                StudyDetail studyDetail = createNewStudyDetail(week, study, now, now.plusDays(7));
                now = now.plusDays(8);
                studyDetails.add(studyDetail);
            }
            List<StudySessionCreateRequest> sessionCreateRequest = createSessionCreateRequest(5);

            // when & then
            assertThatThrownBy(() -> studyDetailValidator.validateUpdateStudyDetail(studyDetails, sessionCreateRequest))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_DETAIL_SESSION_SIZE_MISMATCH.getMessage());
        }

        @Test
        void 요청한_상세정보_id와_기존의_상세정보_id가_맞지_않으면_실패한다() {
            // given
            LocalDateTime now = LocalDateTime.now();
            Member mentor = fixtureHelper.createMentor(1L);
            Study study = fixtureHelper.createStudy(
                    mentor,
                    Period.createPeriod(now.plusDays(5), now.plusDays(10)),
                    Period.createPeriod(now.minusDays(5), now));

            List<StudyDetail> studyDetails = new ArrayList<>();
            for (int i = 2; i <= 5; i++) {
                Long week = (long) i;
                StudyDetail studyDetail = createNewStudyDetail(week, study, now, now.plusDays(7));
                now = now.plusDays(8);
                studyDetails.add(studyDetail);
            }
            List<StudySessionCreateRequest> sessionCreateRequest = createSessionCreateRequest(4);

            // when & then
            assertThatThrownBy(() -> studyDetailValidator.validateUpdateStudyDetail(studyDetails, sessionCreateRequest))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_DETAIL_SESSION_ID_INVALID.getMessage());
        }
    }
}
