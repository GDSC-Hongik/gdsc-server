package com.gdschongik.gdsc.domain.studyv2.dto.dto;

import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;

public record StudySimpleDto(String studyName) {
    public static StudySimpleDto from(StudyV2 study) {
        return new StudySimpleDto(study.getTitle());
    }
}
