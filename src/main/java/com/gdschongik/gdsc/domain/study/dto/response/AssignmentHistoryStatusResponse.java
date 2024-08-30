package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.*;
import com.gdschongik.gdsc.domain.study.domain.vo.Assignment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import java.time.LocalDateTime;

public record AssignmentHistoryStatusResponse(
        Long studyDetailId,
        @Schema(description = "과제 상태") StudyStatus assignmentStatus,
        @Schema(description = "주차") Long week,
        @Nullable @Schema(description = "과제 제목") String title,
        // TODO 추후 처리 예정
        @Nullable @Schema(description = "과제 제출 상태") AssignmentSubmissionStatus assignmentSubmissionStatus,
        @Nullable @Schema(description = "과제 명세 링크") String descriptionLink,
        @Nullable @Schema(description = "마감 기한") LocalDateTime deadline,
        @Nullable @Schema(description = "과제 제출 링크") String submissionLink,
        @Nullable @Schema(description = "과제 제출 실패 사유. 제출 여부도 포함되어 있습니다. 미제출 상태라면 기본 과제 정보만 반환합니다.")
                SubmissionFailureType submissionFailureType,
        @Nullable @Schema(description = "최종 수정 일시") LocalDateTime committedAt) {

    // 과제 제출이 없는 경우, 과제 정보만 사용하여 AssignmentHistoryStatusResponse 생성
    public static AssignmentHistoryStatusResponse of(StudyDetail studyDetail, AssignmentHistory assignmentHistory) {
        if (assignmentHistory == null) {
            return new AssignmentHistoryStatusResponse(
                    studyDetail.getId(),
                    studyDetail.getAssignment().getStatus(),
                    studyDetail.getWeek(),
                    studyDetail.getAssignment().getTitle(),
                    null,
                    studyDetail.getAssignment().getDescriptionLink(),
                    studyDetail.getAssignment().getDeadline(),
                    null,
                    null,
                    null);
        }

        Assignment assignment = studyDetail.getAssignment();
        return new AssignmentHistoryStatusResponse(
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
}
