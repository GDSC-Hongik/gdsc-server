package com.gdschongik.gdsc.domain.study.factory;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.dto.request.StudyCreateRequest;
import com.gdschongik.gdsc.global.annotation.DomainFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

@DomainFactory
public class StudyDomainFactory {

    // 새로운 스터디를 생성합니다.
    public Study createNewStudy(StudyCreateRequest request, Member mentor) {
        LocalDate endDate = request.startDate().plusWeeks(request.totalWeek()).minusDays(1);
        return Study.createStudy(
                request.academicYear(),
                request.semesterType(),
                request.title(),
                mentor,
                Period.createPeriod(request.startDate().atStartOfDay(), endDate.atTime(LocalTime.MAX)),
                Period.createPeriod(
                        request.applicationStartDate().atStartOfDay(),
                        request.applicationEndDate().atTime(LocalTime.MAX)),
                request.totalWeek(),
                request.studyType(),
                request.dayOfWeek(),
                request.studyStartTime().toLocalTime(),
                request.studyEndTime().toLocalTime());
    }

    // 해당 주의 비어있는 스터디상세를 생성합니다.
    public StudyDetail createNoneStudyDetail(Study study, Long week) {
        LocalDateTime startDate = study.getPeriod().getStartDate().plusWeeks((week - 1));
        LocalDateTime endDate = startDate.plusDays(6).toLocalDate().atTime(LocalTime.MAX);

        String attendanceNumber =
                new Random().ints(4, 0, 10).mapToObj(String::valueOf).reduce("", String::concat);
        return StudyDetail.createStudyDetail(study, week, attendanceNumber, Period.createPeriod(startDate, endDate));
    }
}
