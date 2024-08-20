package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.StudyHistory;

public record StudentOngoingStudyResponse(Long studyId) {
    public static StudentOngoingStudyResponse from(StudyHistory studyHistory) {
        if (studyHistory == null) {
            return new StudentOngoingStudyResponse(null);
        }
        return new StudentOngoingStudyResponse(studyHistory.getStudy().getId());
    }
}
