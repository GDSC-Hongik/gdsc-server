package com.gdschongik.gdsc.domain.studyv2.dto.dto;

import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;

public record StudyHistorySimpleDto(String repositoryLink) {
    public static StudyHistorySimpleDto from(StudyHistoryV2 studyHistory) {
        return new StudyHistorySimpleDto(studyHistory.getRepositoryLink());
    }
}
