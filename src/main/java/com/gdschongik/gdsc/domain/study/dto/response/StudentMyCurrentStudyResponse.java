package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.StudyHistory;

public record StudentMyCurrentStudyResponse(Long studyId) {
    public static StudentMyCurrentStudyResponse from(StudyHistory studyHistory) {
        if (studyHistory == null) {
            return new StudentMyCurrentStudyResponse(null);
        }
        return new StudentMyCurrentStudyResponse(studyHistory.getStudy().getId());
    }
}
