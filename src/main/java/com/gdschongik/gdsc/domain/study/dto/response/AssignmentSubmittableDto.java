package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionStatus;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.StudyStatus;
import com.gdschongik.gdsc.domain.study.domain.SubmissionFailureType;
import com.gdschongik.gdsc.domain.study.domain.vo.Assignment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import java.time.LocalDateTime;

public record AssignmentSubmittableDto(
        Long studyDetailId,
        @Schema(description = "과제 상태") StudyStatus assignmentStatus,
        @Schema(description = "주차") Long week,
        @Nullable @Schema(description = "과제 제목") String title,
        @Nullable @Schema(description = "과제 제출 상태") AssignmentSubmissionStatus assignmentSubmissionStatus,
        @Nullable @Schema(description = "과제 명세 링크") String descriptionLink,
        @Nullable @Schema(description = "마감 기한") LocalDateTime deadline,
        @Nullable @Schema(description = "과제 제출 링크") String submissionLink,
        @Nullable @Schema(description = "과제 제출 실패 사유") SubmissionFailureType submissionFailureType,
        @Nullable @Schema(description = "최종 수정 일시") LocalDateTime committedAt) {
    public static AssignmentSubmittableDto of(StudyDetail studyDetail, AssignmentHistory assignmentHistory) {
        Assignment assignment = studyDetail.getAssignment();

        if (assignment.isCanceled()) {
            return canceledAssignment(studyDetail, assignment);
        }

        if (assignmentHistory == null) {
            return beforeAssignmentSubmit(studyDetail, assignment);
        }

        return new AssignmentSubmittableDto(
                studyDetail.getId(),
                assignment.getStatus(),
                studyDetail.getWeek(),
                assignment.getTitle(),
                assignmentHistory.getSubmissionStatus(),
                assignment.getDescriptionLink(),
                assignment.getDeadline(),
                assignmentHistory.getSubmissionLink(),
                assignmentHistory.getSubmissionFailureType(),
                assignmentHistory.getCommittedAt());
    }

    private static AssignmentSubmittableDto canceledAssignment(StudyDetail studyDetail, Assignment assignment) {
        return new AssignmentSubmittableDto(
                studyDetail.getId(),
                assignment.getStatus(),
                studyDetail.getWeek(),
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    private static AssignmentSubmittableDto beforeAssignmentSubmit(StudyDetail studyDetail, Assignment assignment) {
        return new AssignmentSubmittableDto(
                studyDetail.getId(),
                assignment.getStatus(),
                studyDetail.getWeek(),
                assignment.getTitle(),
                null,
                assignment.getDescriptionLink(),
                assignment.getDeadline(),
                null,
                null,
                null);
    }
}
