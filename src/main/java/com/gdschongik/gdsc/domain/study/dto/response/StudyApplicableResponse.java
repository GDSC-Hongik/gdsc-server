package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import java.util.List;

public record StudyApplicableResponse(
        @Nullable @Schema(description = "이미 신청한 스터디 id") Long appliedStudyId, List<StudyResponse> studyResponses) {
    public static StudyApplicableResponse of(Study study, List<StudyResponse> studyResponses) {
        return new StudyApplicableResponse(study == null ? null : study.getId(), studyResponses);
    }
}
