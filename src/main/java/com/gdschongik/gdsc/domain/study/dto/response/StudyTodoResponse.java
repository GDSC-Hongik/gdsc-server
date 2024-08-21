package com.gdschongik.gdsc.domain.study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

public record StudyTodoResponse(
        Long studyDetailId,
        @Schema(description = "현 주차수") Long week,
        @Schema(description = "할일 타입") StudyTodoType todoType,
        @Schema(description = "마감 시각") LocalDateTime deadLine,
        //        @Schema(description = "출석 상태 (출석타입일 때만 사용)") AttendanceStatusResponse attendanceStatus,
        @Schema(description = "과제 제목 (과제타입일 때만 사용)") String assignmentTitle
        //        @Schema(description = "과제 제출 상태 (과제타입일 때만 사용)") AssignmentSubmissionStatusResponse
        // assignmentSubmissionStatus
        ) {

    @Getter
    @AllArgsConstructor
    public enum StudyTodoType {
        ATTENDANCE("출석"),
        ASSIGNMENT("과제");

        private final String value;
    }
}
