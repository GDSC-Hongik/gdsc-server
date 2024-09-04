package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.StudyStatus;
import com.gdschongik.gdsc.domain.study.domain.vo.Assignment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record AssignmentResponse(
        Long studyDetailId,
        @Schema(description = "스터디 이름") String studyTitle,
        @Schema(description = "과제 제목") String title,
        @Schema(description = "마감 기한") LocalDateTime deadline,
        @Schema(description = "주차") Long week,
        @Schema(description = "과제 명세 링크") String descriptionLink,
        @Schema(description = "과제 상태") StudyStatus assignmentStatus,
        @Schema(description = "커리큘럼 시작일") LocalTime curriculumStartAt) {
    public static AssignmentResponse from(StudyDetail studyDetail) {
        Assignment assignment = studyDetail.getAssignment();
        return new AssignmentResponse(
                studyDetail.getId(),
                studyDetail.getStudy().getTitle(),
                assignment.getTitle(),
                assignment.getDeadline(),
                studyDetail.getWeek(),
                assignment.getDescriptionLink(),
                assignment.getStatus(),
                studyDetail.getCurriculum().getStartAt());
    }
}
