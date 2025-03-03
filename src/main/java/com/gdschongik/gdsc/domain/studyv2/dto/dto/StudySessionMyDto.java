package com.gdschongik.gdsc.domain.studyv2.dto.dto;

import com.gdschongik.gdsc.domain.study.domain.StudyType;
import com.gdschongik.gdsc.domain.studyv2.domain.AssignmentHistoryStatus;
import com.gdschongik.gdsc.domain.studyv2.domain.AssignmentHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.AttendanceStatus;
import com.gdschongik.gdsc.domain.studyv2.domain.StudySessionV2;
import jakarta.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Optional;

public record StudySessionMyDto(
        StudySessionStudentDto session,
        AttendanceStatus attendanceStatus,
        AssignmentHistoryStatus assignmentHistoryStatus,
        AssignmentHistoryDto assignmentHistory) {
    public static StudySessionMyDto of(
            StudySessionV2 studySession,
            @Nullable AssignmentHistoryV2 assignmentHistory,
            StudyType studyType,
            boolean isAttended,
            LocalDateTime now) {
        return new StudySessionMyDto(
                StudySessionStudentDto.of(studySession),
                AttendanceStatus.of(studySession, studyType, isAttended, now),
                AssignmentHistoryStatus.of(assignmentHistory, studySession, now),
                Optional.ofNullable(assignmentHistory)
                        .map(AssignmentHistoryDto::from)
                        .orElse(null));
    }
}
