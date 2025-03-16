package com.gdschongik.gdsc.domain.studyv2.dto.response;

import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;
import java.util.List;

public record StudentStudyMyCurrentResponse(List<Long> studyIds) {
    public static StudentStudyMyCurrentResponse from(List<StudyHistoryV2> studyHistories) {
        List<Long> studyIds = studyHistories.stream()
                .map(studyHistory -> studyHistory.getStudy().getId())
                .toList();
        return new StudentStudyMyCurrentResponse(studyIds);
    }
}
