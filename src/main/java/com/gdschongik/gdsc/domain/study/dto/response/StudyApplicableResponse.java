package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.Study;
import jakarta.annotation.Nullable;
import java.util.List;

public record StudyApplicableResponse(@Nullable Long studyId, List<StudyResponse> studyResponses) {
    public static StudyApplicableResponse of(Study study, List<StudyResponse> studyResponses) {
        return new StudyApplicableResponse(study == null ? null : study.getId(), studyResponses);
    }
}
