package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.vo.Session;
import java.time.format.DateTimeFormatter;

public record SessionResponse(
        Long studyDetailId, String period, String week, String title, String description, String difficulty) {

    // TODO 포매터 분리하기
    public static SessionResponse from(StudyDetail studyDetail) {
        Session session = studyDetail.getSession();
        return new SessionResponse(
                studyDetail.getId(),
                studyDetail.getPeriod().getStartDate().format(DateTimeFormatter.ofPattern("MM.dd")) + "-"
                        + studyDetail.getPeriod().getEndDate().format(DateTimeFormatter.ofPattern("MM.dd")),
                studyDetail.getWeek().toString() + "주차",
                session.getTitle(),
                session.getDescription(),
                session.getDifficulty().toString());
    }
}
