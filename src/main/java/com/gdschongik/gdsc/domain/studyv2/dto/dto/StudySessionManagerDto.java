package com.gdschongik.gdsc.domain.studyv2.dto.dto;

import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.studyv2.domain.StudySessionV2;

/**
 * 스터디 회차 관리자 DTO입니다. 출결번호가 포함되어 있습니다.
 */
public record StudySessionManagerDto(
        Long studySessionId,
        Integer position,
        String title,
        String description,
        String lessonAttendanceNumber,
        Period lessonPeriod,
        String assignmentDescriptionLink,
        Period assignmentPeriod,
        Long studyId) {
    public static StudySessionManagerDto from(StudySessionV2 studySession) {
        return new StudySessionManagerDto(
                studySession.getId(),
                studySession.getPosition(),
                studySession.getTitle(),
                studySession.getDescription(),
                studySession.getLessonAttendanceNumber(),
                studySession.getLessonPeriod(),
                studySession.getAssignmentDescriptionLink(),
                studySession.getAssignmentPeriod(),
                studySession.getStudyV2().getId());
    }
}
