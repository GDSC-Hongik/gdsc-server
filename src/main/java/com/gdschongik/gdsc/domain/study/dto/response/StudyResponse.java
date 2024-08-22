package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.study.domain.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record StudyResponse(
        Long studyId,
        @Schema(description = "학년도") Integer academicYear,
        @Schema(description = "학기") SemesterType semesterType,
        @Schema(description = "이름") String title,
        @Schema(description = "종류") String studyType,
        @Schema(description = "상세설명 노션 링크") String notionLink,
        @Schema(description = "한 줄 소개") String introduction,
        @Schema(description = "멘토 이름") String mentorName,
        @Schema(description = "스터디 요일") DayOfWeek dayOfWeek,
        @Schema(description = "스터디 시작 시간") LocalTime startTime,
        @Schema(description = "스터디 종료 시간") LocalTime endTime,
        @Schema(description = "총 주차수") Long totalWeek,
        @Schema(description = "개강일") LocalDateTime openingDate,
        @Schema(description = "신청 종료일") LocalDateTime applicationEndDate) {

    public static StudyResponse from(Study study) {
        return new StudyResponse(
                study.getId(),
                study.getAcademicYear(),
                study.getSemesterType(),
                study.getTitle(),
                study.getStudyType().getValue(),
                study.getNotionLink(),
                study.getIntroduction(),
                study.getMentor().getName(),
                study.getDayOfWeek(),
                study.getStartTime(),
                study.getEndTime(),
                study.getTotalWeek(),
                study.getPeriod().getStartDate(),
                study.getApplicationPeriod().getEndDate());
    }
}
