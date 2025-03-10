package com.gdschongik.gdsc.domain.studyv2.dto.response;

import com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionStatus;
import com.gdschongik.gdsc.domain.study.domain.StudyType;
import com.gdschongik.gdsc.domain.study.dto.response.AssignmentSubmissionStatusResponse;
import com.gdschongik.gdsc.domain.study.dto.response.AttendanceStatusResponse;
import com.gdschongik.gdsc.domain.studyv2.domain.*;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.AssignmentHistoryDto;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudySessionStudentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.gdschongik.gdsc.domain.studyv2.dto.response.StudyTodoResponse.StudyTodoType.ASSIGNMENT;
import static com.gdschongik.gdsc.domain.studyv2.dto.response.StudyTodoResponse.StudyTodoType.ATTENDANCE;

public record StudyTodoResponse(
        @Schema(description = "스터디 세션 정보") StudySessionStudentDto studySession,
        @Schema(description = "할 일 타입") StudyTodoResponse.StudyTodoType todoType,
        @Schema(description = "마감 시각") LocalDateTime deadLine, // 마감 시각이 lesson 의 end 인지, assignment end 인지 판단은 프론트보다 백이 하는 것이 맞다고 생각해서 놔둠. (PR에 명시)
        @Schema(description = "출석 상태 (출석타입일 때만 사용)") AttendanceStatus attendanceStatus,
        @Schema(description = "과제 정보 (과제타입일 때만 사용)") AssignmentHistoryDto assignmentHistory,
        @Schema(description = "과제 제출 상태 (과제타입일 때만 사용)") AssignmentHistoryStatus assignmentHistoryStatus
) {
    public static StudyTodoResponse attendanceType(StudySessionV2 studySession, StudyType studyType, List<AttendanceV2> attendances, LocalDateTime now) {
        return new StudyTodoResponse(
                StudySessionStudentDto.of(studySession),
                ATTENDANCE,
                studySession.getLessonPeriod().getEndDate(),
                AttendanceStatus.of(studySession, studyType, isAttended(studySession, attendances), now),
                null,
                null
        );
    }

    public static StudyTodoResponse assignmentType(AssignmentHistoryV2 assignmentHistory, LocalDateTime now) {
        StudySessionV2 studySession = assignmentHistory.getStudySession();
        return new StudyTodoResponse(
                StudySessionStudentDto.of(studySession),
                ASSIGNMENT,
                studySession.getAssignmentPeriod().getEndDate(),
                null,
                AssignmentHistoryDto.from(assignmentHistory),
                AssignmentHistoryStatus.of(assignmentHistory, studySession, now)
        );
    }

    private static boolean isAttended(StudySessionV2 studySession, List<AttendanceV2> attendances) {
        return attendances.stream()
                .anyMatch(attendance -> attendance.getStudySession().getId().equals(studySession.getId()));
    }

    @Getter
    @RequiredArgsConstructor
    public enum StudyTodoType {
        ATTENDANCE("출석"), ASSIGNMENT("과제");

        private final String value;
    }
}
