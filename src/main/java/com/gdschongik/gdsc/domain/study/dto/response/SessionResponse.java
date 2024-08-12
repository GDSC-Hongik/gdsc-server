package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.domain.study.domain.Difficulty;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.vo.Session;

public record SessionResponse(
        Long studyDetailId, Period period, Long week, String title, String description, Difficulty difficulty) {
    public static SessionResponse from(StudyDetail studyDetail) {
        Session session = studyDetail.getSession();
        return new SessionResponse(
                studyDetail.getId(),
                studyDetail.getPeriod(),
                studyDetail.getWeek(),
                session.getTitle(),
                session.getDescription(),
                session.getDifficulty());
    }
}
