package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.StudyStatus;
import com.gdschongik.gdsc.domain.study.domain.vo.Assignment;
import io.swagger.v3.oas.annotations.media.Schema;

public record AssignmentResponse(
        Long studyDetailId,
        @Schema(description = "과제 제목") String title,
        @Schema(description = "마감 기한") String deadline,
        @Schema(description = "주차") String week,
        @Schema(description = "과제 명세 링크") String descriptionLink,
        @Schema(description = "과제 상태") StudyStatus assignmentStatus) {
    public static AssignmentResponse from(StudyDetail studyDetail) {
        Assignment assignment = studyDetail.getAssignment();
        return new AssignmentResponse(
                studyDetail.getId(),
                assignment.getTitle(),
                assignment.getDeadline().toString(),
                studyDetail.getWeek() + "주차",
                assignment.getDescriptionLink(),
                assignment.getStatus());
    }
}
