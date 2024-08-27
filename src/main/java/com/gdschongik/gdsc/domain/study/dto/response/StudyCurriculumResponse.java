package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.domain.study.domain.Difficulty;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.vo.Curriculum;

public record StudyCurriculumResponse(
        Long studyDetailId, Period period, Long week, String title, String description, Difficulty difficulty) {

    public static StudyCurriculumResponse from(StudyDetail studyDetail) {
        Curriculum curriculum = studyDetail.getCurriculum();
        return new StudyCurriculumResponse(
                studyDetail.getId(),
                studyDetail.getPeriod(),
                studyDetail.getWeek(),
                curriculum.getTitle(),
                curriculum.getDescription(),
                curriculum.getDifficulty());
    }
}