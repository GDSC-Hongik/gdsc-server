package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dto.request.AssignmentCreateUpdateRequest;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDateTime;

@DomainService
public class StudyDetailValidator {

    public void validateCancelStudyAssignment(Member member, StudyDetail studyDetail) {
        validateStudyMentorAuthorization(member, studyDetail);
    }

    public void validatePublishStudyAssignment(
            Member member, StudyDetail studyDetail, AssignmentCreateUpdateRequest request) {
        validateStudyMentorAuthorization(member, studyDetail);
        validateDeadLine(request.deadLine());
    }

    // 해당 스터디의 멘토가 아니라면 스터디에 대한 권한이 없다.
    private void validateStudyMentorAuthorization(Member member, StudyDetail studyDetail) {
        if (!member.getId().equals(studyDetail.getStudy().getMentor().getId())) {
            throw new CustomException(STUDY_DETAIL_UPDATE_RESTRICTED_TO_MENTOR);
        }
    }

    private void validateDeadLine(LocalDateTime deadline) {
        if (deadline.isBefore(LocalDateTime.now())) {
            throw new CustomException(ASSIGNMENT_DEADLINE_INVALID);
        }
    }

    public void validateUpdateStudyAssignment(
            Member currentMember, StudyDetail studyDetail, AssignmentCreateUpdateRequest request) {

        validateStudyMentorAuthorization(currentMember, studyDetail);
        validateUpdateDeadline(LocalDateTime.now(), studyDetail.getAssignment().getDeadline(), request.deadLine());
    }

    /**
     * 과제 마감기한이 수정 시점보다 앞서거나 수정할 마감기한이 기존 마감기한보다 앞서면 안된다.
     */
    private void validateUpdateDeadline(
            LocalDateTime currentTime, LocalDateTime deadLine, LocalDateTime updateDeadLine) {
        if (currentTime.isAfter(deadLine) || deadLine.isAfter(updateDeadLine)) {
            throw new CustomException(STUDY_DETAIL_ASSIGNMENT_INVALID_DEADLINE);
        }
    }
}
