package com.gdschongik.gdsc.domain.studyv2.dto.dto;

import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.studyv2.domain.StudySessionV2;

/**
 * 스터디 회차 학생 DTO입니다. 출결번호가 포함되어 있지 않습니다.
 */
public record StudySessionStudentDto(
        Long studySessionId,
        Integer position,
        String title,
        String description,
        Period lessonPeriod,
        String assignmentDescriptionLink,
        Period assignmentPeriod,
        Long studyId) {
    public static StudySessionStudentDto of(StudySessionV2 studySession) {
        return new StudySessionStudentDto(
                studySession.getId(),
                studySession.getPosition(),
                studySession.getTitle(),
                studySession.getDescription(),
                studySession.getLessonPeriod(),
                studySession.getAssignmentDescriptionLink(),
                studySession.getAssignmentPeriod(),
                studySession.getStudy().getId());
    }
}
