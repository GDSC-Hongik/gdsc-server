package com.gdschongik.gdsc.domain.studyv2.dto.dto;

import com.gdschongik.gdsc.domain.common.vo.Semester;
import com.gdschongik.gdsc.domain.study.domain.StudyType;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;

public record StudySimpleDto(
        Long studyId, String studyName, StudyType studyType, Semester semester, Long mentorId, String mentorName) {
    public static StudySimpleDto from(StudyV2 study) {
        return new StudySimpleDto(
                study.getId(),
                study.getTitle(),
                study.getType(),
                study.getSemester(),
                study.getMentor().getId(),
                study.getMentor().getName());
    }
}
