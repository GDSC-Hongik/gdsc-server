package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record AssignmentHistoryResponse(
        Long assignmentHistoryId,
        @Schema(description = "과제 제목") String title,
        @Schema(description = "마감 기한") LocalDateTime deadline,
        @Schema(description = "과제 명세 링크") String descriptionLink,
        @Schema(description = "과제 제출 상태") AssignmentSubmissionStatus assignmentSubmissionStatus,
        @Schema(description = "주차") Long week) {
    public static AssignmentHistoryResponse from(AssignmentHistory assignmentHistory) {
        return new AssignmentHistoryResponse(
                assignmentHistory.getId(),
                assignmentHistory.getStudyDetail().getAssignment().getTitle(),
                assignmentHistory.getStudyDetail().getAssignment().getDeadline(),
                assignmentHistory.getStudyDetail().getAssignment().getDescriptionLink(),
                assignmentHistory.getSubmissionStatus(),
                assignmentHistory.getStudyDetail().getWeek());
    }
}
