package com.gdschongik.gdsc.domain.studyv2.dto.response;

import com.gdschongik.gdsc.domain.studyv2.domain.StudySessionV2;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record AssignmentResponse(
        Long studySessionId,
        @Schema(description = "스터디 이름") String studyTitle,
        @Schema(description = "과제 제목") String title,
        @Schema(description = "회차") Integer position,
        @Schema(description = "과제 명세 링크") String descriptionLink,
        @Schema(description = "과제 시작일") LocalDateTime assignmentStartDate,
        @Schema(description = "과제 종료일") LocalDateTime assignmentEndDate) {
    public static AssignmentResponse from(StudySessionV2 studySession) {
        return new AssignmentResponse(
                studySession.getId(),
                studySession.getStudy().getTitle(),
                studySession.getTitle(),
                studySession.getPosition(),
                studySession.getAssignmentDescriptionLink(),
                studySession.getAssignmentPeriod().getStartDate(),
                studySession.getAssignmentPeriod().getEndDate());
    }
}
