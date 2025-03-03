package com.gdschongik.gdsc.domain.studyv2.dto.dto;

import com.gdschongik.gdsc.domain.study.domain.StudyHistoryStatus;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;

public record StudyHistoryDto(Long studyHistoryId, StudyHistoryStatus status, Long memberId, Long studyId) {
    public static StudyHistoryDto from(StudyHistoryV2 studyHistory) {
        return new StudyHistoryDto(
                studyHistory.getId(),
                studyHistory.getStatus(),
                studyHistory.getStudent().getId(),
                studyHistory.getStudy().getId());
    }
}
