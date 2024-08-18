package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.*;
import com.gdschongik.gdsc.domain.study.domain.vo.Assignment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import java.time.LocalDateTime;

public record AssignmentStatusResponse(
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
    public static AssignmentStatusResponse from(AssignmentHistory assignmentHistory) {
        StudyDetail studyDetail = assignmentHistory.getStudyDetail();
        Assignment assignment = studyDetail.getAssignment();
        return new AssignmentStatusResponse(
                studyDetail.getId(),
                assignment.getStatus(),
                studyDetail.getWeek(),
                assignment.getTitle(),
                assignmentHistory.getSubmissionStatus(),
                assignment.getDescriptionLink(),
                assignment.getDeadline(),
                assignmentHistory.getSubmissionLink(),
                assignmentHistory.getSubmissionFailureType() == null
                        ? null
                        : assignmentHistory.getSubmissionFailureType(),
                assignmentHistory.getCommittedAt() == null ? null : assignmentHistory.getCommittedAt());
    }
}
