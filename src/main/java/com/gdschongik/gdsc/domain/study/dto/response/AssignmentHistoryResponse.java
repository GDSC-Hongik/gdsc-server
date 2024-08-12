package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import com.gdschongik.gdsc.domain.study.domain.StudyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.format.DateTimeFormatter;

public record AssignmentHistoryResponse(
        Long assignmentHistoryId,
        @Schema(description = "과제 제목") String title,
        @Schema(description = "마감 기한") String deadline,
        @Schema(description = "과제 명세 링크") String descriptionLink,
        @Schema(description = "과제 상태") StudyStatus assignmentStatus,
        @Schema(description = "주차") String week) {
    public static AssignmentHistoryResponse from(AssignmentHistory assignmentHistory) {
        return new AssignmentHistoryResponse(
                assignmentHistory.getId(),
                assignmentHistory.getStudyDetail().getAssignment().getTitle(),
                "종료 : "
                        + assignmentHistory
                                .getStudyDetail()
                                .getAssignment()
                                .getDeadline()
                                .format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 HH:mm")),
                assignmentHistory.getStudyDetail().getAssignment().getDescriptionLink(),
                assignmentHistory.getStudyDetail().getAssignment().getStatus(),
                assignmentHistory.getStudyDetail().getWeek().toString() + "주차");
    }
}
