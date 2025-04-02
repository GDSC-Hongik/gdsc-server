package com.gdschongik.gdsc.domain.study.domain.service;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.dto.request.AssignmentCreateUpdateRequest;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDateTime;
import java.util.Set;

@DomainService
public class StudyDetailValidator {

    public void validateCancelStudyAssignment(Member member, StudyDetail studyDetail) {
        validateStudyMentorAuthorization(member, studyDetail);
    }

    public void validatePublishStudyAssignment(
            Member member, StudyDetail studyDetail, AssignmentCreateUpdateRequest request, LocalDateTime now) {
        validateStudyMentorAuthorization(member, studyDetail);
        validateDeadLine(request.deadLine(), studyDetail.getPeriod().getStartDate(), now);
    }

    // 해당 스터디의 멘토가 아니라면 스터디에 대한 권한이 없다.
    private void validateStudyMentorAuthorization(Member member, StudyDetail studyDetail) {
        if (!member.getId().equals(studyDetail.getStudy().getMentor().getId())) {
            throw new CustomException(STUDY_DETAIL_UPDATE_RESTRICTED_TO_MENTOR);
        }
    }

    private void validateDeadLine(LocalDateTime deadline, LocalDateTime studyStartDate, LocalDateTime now) {
        if (deadline.isBefore(now) || deadline.isBefore(studyStartDate)) {
            throw new CustomException(ASSIGNMENT_DEADLINE_INVALID);
        }
    }

    public void validateUpdateStudyAssignment(
            Member currentMember, StudyDetail studyDetail, AssignmentCreateUpdateRequest request, LocalDateTime now) {

        validateStudyMentorAuthorization(currentMember, studyDetail);
        validateUpdateDeadline(now, studyDetail.getAssignment().getDeadline(), request.deadLine());
    }

    /**
     * 과제 마감기한이 수정 시점보다 앞서거나 수정할 마감기한이 기존 마감기한보다 앞서면 안된다.
     */
    private void validateUpdateDeadline(
            LocalDateTime currentTime, LocalDateTime deadLine, LocalDateTime updateDeadLine) {
        if (currentTime.isAfter(deadLine)) {
            throw new CustomException(STUDY_DETAIL_ASSIGNMENT_INVALID_DEADLINE);
        }

        if (deadLine.isAfter(updateDeadLine)) {
            throw new CustomException(STUDY_DETAIL_ASSIGNMENT_INVALID_UPDATE_DEADLINE);
        }
    }

    public void validateUpdateStudyDetail(Set<Long> studyDetails, Set<Long> requests) {
        // StudyDetail 목록과 요청된 StudyCurriculumCreateRequest 목록의 크기를 먼저 비교
        if (studyDetails.size() != requests.size()) {
            throw new CustomException(STUDY_DETAIL_CURRICULUM_SIZE_MISMATCH);
        }

        // 두 ID 집합이 동일한지 비교하여 ID 불일치 시 예외를 던짐
        if (!studyDetails.equals(requests)) {
            throw new CustomException(STUDY_DETAIL_ID_INVALID);
        }
    }
}
