package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dto.request.AssignmentCreateUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.request.StudySessionCreateRequest;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        if (currentTime.isAfter(deadLine)) {
            throw new CustomException(STUDY_DETAIL_ASSIGNMENT_INVALID_DEADLINE);
        }

        if (deadLine.isAfter(updateDeadLine)) {
            throw new CustomException(STUDY_DETAIL_ASSIGNMENT_INVALID_UPDATE_DEADLINE);
        }
    }

    public void validateUpdateStudyDetail(List<StudyDetail> studyDetails, List<StudySessionCreateRequest> requests) {
        // StudyDetail 목록과 요청된 StudySessionCreateRequest 목록의 크기를 먼저 비교
        if (studyDetails.size() != requests.size()) {
            throw new CustomException(STUDY_DETAIL_SESSION_SIZE_MISMATCH);
        }

        // StudyDetail ID를 추출하여 Set으로 저장
        Set<Long> studyDetailIds = studyDetails.stream().map(StudyDetail::getId).collect(Collectors.toSet());

        // 요청된 StudySessionCreateRequest의 StudyDetail ID를 추출하여 Set으로 저장
        Set<Long> requestIds =
                requests.stream().map(StudySessionCreateRequest::studyDetailId).collect(Collectors.toSet());

        // 두 ID 집합이 동일한지 비교하여 ID 불일치 시 예외를 던짐
        if (!studyDetailIds.equals(requestIds)) {
            throw new CustomException(STUDY_DETAIL_SESSION_ID_INVALID);
        }
    }
}
