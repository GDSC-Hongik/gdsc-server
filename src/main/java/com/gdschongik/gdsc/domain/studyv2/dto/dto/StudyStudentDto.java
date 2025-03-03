package com.gdschongik.gdsc.domain.studyv2.dto.dto;

import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.common.vo.Semester;
import com.gdschongik.gdsc.domain.study.domain.StudyType;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * 스터디 학생 DTO입니다. 디스코드 관련 ID가 포함되어 있지 않습니다.
 */
public record StudyStudentDto(
        Long studyId,
        StudyType type,
        String title,
        String description,
        String descriptionNotionLink,
        Semester semester,
        Integer totalRound,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        Period applicationPeriod,
        Long mentorId,
        String mentorName) {
    public static StudyStudentDto from(StudyV2 study) {
        return new StudyStudentDto(
                study.getId(),
                study.getType(),
                study.getTitle(),
                study.getDescription(),
                study.getDescriptionNotionLink(),
                study.getSemester(),
                study.getTotalRound(),
                study.getDayOfWeek(),
                study.getStartTime(),
                study.getEndTime(),
                study.getApplicationPeriod(),
                study.getMentor().getId(),
                study.getMentor().getName());
    }
}
