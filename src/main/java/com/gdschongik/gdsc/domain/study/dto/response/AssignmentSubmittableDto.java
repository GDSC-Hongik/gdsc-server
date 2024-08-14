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
        @Nullable @Schema(description = "제출한 과제") String submittedAssignment,
        @Nullable @Schema(description = "과제 제출 링크") String submissionLink,
        @Nullable @Schema(description = "과제 제출 실패 사유") SubmissionFailureType submissionFailureType) {
    public static AssignmentSubmittableDto from(AssignmentHistory assignmentHistory) {
        StudyDetail studyDetail = assignmentHistory.getStudyDetail();
        Assignment assignment = studyDetail.getAssignment();
        boolean isCancelled = assignment.isCancelled();

        return new AssignmentSubmittableDto(
                studyDetail.getId(),
                assignment.getStatus(),
                studyDetail.getWeek(),
                isCancelled ? null : assignment.getTitle(),
                isCancelled ? null : assignmentHistory.getSubmissionStatus(),
                isCancelled ? null : assignment.getDescriptionLink(),
                isCancelled ? null : assignment.getDeadline(),
                isCancelled ? null : getSubmittedAssignment(assignmentHistory.getSubmissionLink()),
                isCancelled ? null : assignmentHistory.getSubmissionLink(),
                isCancelled
                        ? null
                        : assignmentHistory.getSubmissionFailureType() == null
                                ? null
                                : assignmentHistory.getSubmissionFailureType());
    }

    private static String getSubmittedAssignment(String submissionLink) {
        return submissionLink.split("/")[3] + "/" + submissionLink.split("/")[4];
    }
}
