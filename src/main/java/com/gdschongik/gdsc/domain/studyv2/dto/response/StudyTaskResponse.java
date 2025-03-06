package com.gdschongik.gdsc.domain.studyv2.dto.response;

import static com.gdschongik.gdsc.domain.studyv2.dto.response.StudyTaskResponse.StudyTaskType.*;

import com.gdschongik.gdsc.domain.studyv2.domain.AssignmentHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudySessionV2;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record StudyTaskResponse(
        Long studySessionId,
        @Schema(description = "현 회차수") Integer round,
        @Schema(description = "태스크 타입") StudyTaskType taskType,
        @Schema(description = "마감 시각") LocalDateTime deadLine,
        @Schema(description = "출석 상태 (출석타입일 때만 사용)") AttendanceStatusResponse attendanceStatus,
        @Schema(description = "과제 제목 (과제타입일 때만 사용)") String assignmentTitle,
        @Schema(description = "과제 제출 상태 (과제타입일 때만 사용)") AssignmentSubmissionStatusResponse assignmentSubmissionStatus) {

    public static StudyTaskResponse createAttendanceType(
            StudySessionV2 studySession, LocalDate now, boolean isAttended) {
        return new StudyTaskResponse(
                studySession.getId(),
                studySession.getPosition(),
                ATTENDANCE,
                studySession.getLessonPeriod().getEndDate(),
                AttendanceStatusResponse.of(studySession, now, isAttended),
                null,
                null);
    }

    public static StudyTaskResponse createAssignmentType(
            StudySessionV2 studySession, AssignmentHistoryV2 assignmentHistory) {
        return new StudyTaskResponse(
                studySession.getId(),
                studySession.getPosition(),
                ASSIGNMENT,
                studySession.getAssignmentPeriod().getEndDate(),
                null,
                studySession.getTitle(),
                AssignmentSubmissionStatusResponse.of(assignmentHistory, studySession));
    }

    @Getter
    @RequiredArgsConstructor
    public enum StudyTaskType {
        ATTENDANCE("출석"),
        ASSIGNMENT("과제");

        private final String value;
    }
}
