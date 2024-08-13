package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.domain.study.domain.Difficulty;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.vo.Session;

public record StudySessionResponse(Long studyDetailId, Period period, Long week, String title, Difficulty difficulty) {

    public static StudySessionResponse from(StudyDetail studyDetail) {
        Session session = studyDetail.getSession();
        return new StudySessionResponse(
                studyDetail.getId(),
                studyDetail.getPeriod(),
                studyDetail.getWeek(),
                session.getTitle(),
                session.getDifficulty());
    }
}
