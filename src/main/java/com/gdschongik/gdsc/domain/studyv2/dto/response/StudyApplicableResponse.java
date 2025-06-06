package com.gdschongik.gdsc.domain.studyv2.dto.response;

import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudyCommonDto;
import java.util.List;

public record StudyApplicableResponse(List<Long> appliedStudyIds, List<StudyCommonDto> applicableStudies) {
    public static StudyApplicableResponse of(List<StudyHistoryV2> studyHistories, List<StudyV2> applicableStudies) {
        return new StudyApplicableResponse(
                studyHistories.stream()
                        .map(studyHistory -> studyHistory.getStudy().getId())
                        .toList(),
                applicableStudies.stream().map(StudyCommonDto::from).toList());
    }
}
