package com.gdschongik.gdsc.domain.study.dto.response;

import static com.gdschongik.gdsc.domain.study.dto.response.StudyTaskResponse.StudyTaskType.ASSIGNMENT;
import static com.gdschongik.gdsc.domain.study.dto.response.StudyTaskResponse.StudyTaskType.ATTENDANCE;

import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record StudyTaskResponse(
        Long studyDetailId,
        @Schema(description = "현 주차수") Long week,
        @Schema(description = "태스크 타입") StudyTaskType taskType,
        @Schema(description = "마감 시각") LocalDateTime deadLine,
        @Schema(description = "출석 상태 (출석타입일 때만 사용)") AttendanceStatusResponse attendanceStatus,
        @Schema(description = "과제 제목 (과제타입일 때만 사용)") String assignmentTitle,
        @Schema(description = "과제 제출 상태 (과제타입일 때만 사용)") AssignmentSubmissionStatusResponse assignmentSubmissionStatus) {

    public static StudyTaskResponse createAttendanceType(StudyDetail studyDetail, LocalDate now, boolean isAttended) {
        if (studyDetail.getCurriculum().isCancelled()) {
            return new StudyTaskResponse(
                    studyDetail.getId(),
                    studyDetail.getWeek(),
                    ATTENDANCE,
                    null,
                    AttendanceStatusResponse.CANCELLED,
                    null,
                    null);
        }
        return new StudyTaskResponse(
                studyDetail.getId(),
                studyDetail.getWeek(),
                ATTENDANCE,
                studyDetail.getAttendanceDay().atTime(23, 59, 59),
                AttendanceStatusResponse.of(studyDetail, now, isAttended),
                null,
                null);
    }

    public static StudyTaskResponse createAssignmentType(StudyDetail studyDetail, AssignmentHistory assignmentHistory) {
        if (studyDetail.getAssignment().isCancelled()) {
            return new StudyTaskResponse(
                    studyDetail.getId(),
                    studyDetail.getWeek(),
                    ASSIGNMENT,
                    null,
                    null,
                    null,
                    AssignmentSubmissionStatusResponse.of(null, studyDetail));
        }

        return new StudyTaskResponse(
                studyDetail.getId(),
                studyDetail.getWeek(),
                ASSIGNMENT,
                studyDetail.getAssignment().getDeadline(),
                null,
                studyDetail.getAssignment().getTitle(),
                AssignmentSubmissionStatusResponse.of(assignmentHistory, studyDetail));
    }

    public boolean isAttendance() {
        return taskType == ATTENDANCE;
    }

    public boolean isAssignment() {
        return taskType == ASSIGNMENT;
    }

    @Getter
    @RequiredArgsConstructor
    public enum StudyTaskType {
        ATTENDANCE("출석"),
        ASSIGNMENT("과제");

        private final String value;
    }
}
