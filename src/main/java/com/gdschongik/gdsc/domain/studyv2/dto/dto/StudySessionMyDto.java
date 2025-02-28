package com.gdschongik.gdsc.domain.studyv2.dto.dto;

import com.gdschongik.gdsc.domain.studyv2.domain.AttendanceStatus;

public record StudySessionMyDto(
        StudySessionStudentDto session, AttendanceStatus attendanceStatus, AssignmentHistoryDto assignmentHistory) {}
