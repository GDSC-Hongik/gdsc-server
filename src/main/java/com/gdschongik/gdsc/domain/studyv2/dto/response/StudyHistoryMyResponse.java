package com.gdschongik.gdsc.domain.studyv2.dto.response;

import com.gdschongik.gdsc.domain.studyv2.domain.StudyAchievementV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudyAchievementDto;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudyCommonDto;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudyHistoryDto;
import java.util.List;

public record StudyHistoryMyResponse(
        StudyHistoryDto studyHistory, StudyCommonDto study, List<StudyAchievementDto> achievements) {
    public static StudyHistoryMyResponse of(
            StudyHistoryV2 studyHistory, StudyV2 study, List<StudyAchievementV2> achievements) {
        return new StudyHistoryMyResponse(
                StudyHistoryDto.from(studyHistory),
                StudyCommonDto.from(study),
                achievements.stream().map(StudyAchievementDto::from).toList());
    }
}
