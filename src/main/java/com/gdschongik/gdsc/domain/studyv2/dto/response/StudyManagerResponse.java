package com.gdschongik.gdsc.domain.studyv2.dto.response;

import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudyManagerDto;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudySessionManagerDto;
import java.util.List;

public record StudyManagerResponse(StudyManagerDto study, List<StudySessionManagerDto> studySessions) {
    public static StudyManagerResponse from(StudyV2 study) {
        return new StudyManagerResponse(
                StudyManagerDto.from(study),
                study.getStudySessions().stream()
                        .map(StudySessionManagerDto::from)
                        .toList());
    }
}
