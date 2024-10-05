package com.gdschongik.gdsc.domain.study.dto.response;

import static com.gdschongik.gdsc.domain.study.dto.response.StudyTodoResponse.StudyTodoType.ASSIGNMENT;
import static com.gdschongik.gdsc.domain.study.dto.response.StudyTodoResponse.StudyTodoType.ATTENDANCE;

import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// todo: 활용이 다양해졌으므로 rename 필요
public record StudyTodoResponse(
        Long studyDetailId,
        @Schema(description = "현 주차수") Long week,
        @Schema(description = "할일 타입") StudyTodoType todoType,
        @Schema(description = "마감 시각") LocalDateTime deadLine,
        @Schema(description = "출석 상태 (출석타입일 때만 사용)") AttendanceStatusResponse attendanceStatus,
        @Schema(description = "과제 제목 (과제타입일 때만 사용)") String assignmentTitle,
        @Schema(description = "과제 제출 상태 (과제타입일 때만 사용)") AssignmentSubmissionStatusResponse assignmentSubmissionStatus) {

    public static StudyTodoResponse createAttendanceType(StudyDetail studyDetail, LocalDate now, boolean isAttended) {
        return new StudyTodoResponse(
                studyDetail.getId(),
                studyDetail.getWeek(),
                ATTENDANCE,
                studyDetail.getAttendanceDay().atTime(23, 59, 59),
                AttendanceStatusResponse.of(studyDetail, now, isAttended),
                null,
                null);
    }

    public static StudyTodoResponse createAssignmentType(StudyDetail studyDetail, AssignmentHistory assignmentHistory) {
        if (studyDetail.getAssignment().isCancelled()) {
            return new StudyTodoResponse(
                    studyDetail.getId(),
                    studyDetail.getWeek(),
                    ASSIGNMENT,
                    null,
                    null,
                    null,
                    AssignmentSubmissionStatusResponse.getCancelled());
        }

        return new StudyTodoResponse(
                studyDetail.getId(),
                studyDetail.getWeek(),
                ASSIGNMENT,
                studyDetail.getAssignment().getDeadline(),
                null,
                studyDetail.getAssignment().getTitle(),
                AssignmentSubmissionStatusResponse.from(assignmentHistory));
    }

    @Getter
    @RequiredArgsConstructor
    public enum StudyTodoType {
        ATTENDANCE("출석"),
        ASSIGNMENT("과제");

        private final String value;
    }
}
