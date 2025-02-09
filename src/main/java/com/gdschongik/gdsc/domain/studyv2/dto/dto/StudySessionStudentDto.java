package com.gdschongik.gdsc.domain.studyv2.dto.dto;

/**
 * 스터디 회차 학생 DTO입니다. 출결번호가 포함되어 있지 않습니다.
 */
public record StudySessionStudentDto(
        Long studySessionId,
        Integer position,
        String title,
        String description,
        String lessonAttendanceStatus,
        String assignmentDescriptionLink,
        String assignmentStatus,
        Long studyId) {}
