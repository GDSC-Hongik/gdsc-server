package com.gdschongik.gdsc.domain.studyv2.dto.dto;

import com.gdschongik.gdsc.domain.common.vo.Period;

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
        Long studyId) {}
