package com.gdschongik.gdsc.domain.studyv2.dto.dto;

import com.gdschongik.gdsc.domain.study.domain.StudyHistoryStatus;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;

public record StudyHistoryStudentDto(Long studyHistoryId, StudyHistoryStatus status, Long memberId, Long studyId) {
    public static StudyHistoryStudentDto from(StudyHistoryV2 studyHistory) {
        return new StudyHistoryStudentDto(
                studyHistory.getId(),
                studyHistory.getStatus(),
                studyHistory.getStudent().getId(),
                studyHistory.getStudy().getId());
    }
}
