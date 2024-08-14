package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.StudyStatus;
import com.gdschongik.gdsc.domain.study.domain.vo.Assignment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record AssignmentResponse(
        Long studyDetailId,
        @Schema(description = "과제 제목") String title,
        @Schema(description = "마감 기한") LocalDateTime deadline,
        @Schema(description = "과제 명세 링크") String descriptionLink,
        @Schema(description = "과제 상태") StudyStatus assignmentStatus) {
    public static AssignmentResponse from(StudyDetail studyDetail) {
        Assignment assignment = studyDetail.getAssignment();
        return new AssignmentResponse(
                studyDetail.getId(),
                assignment.getTitle(),
                assignment.getDeadline(),
                assignment.getDescriptionLink(),
                assignment.getStatus());
    }
}
