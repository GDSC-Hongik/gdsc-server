package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.study.domain.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.DayOfWeek;
import java.time.LocalTime;

public record CommonStudyResponse(
        Long studyId,
        @Schema(description = "이름") String title,
        @Schema(description = "활동 년도") Integer academicYear,
        @Schema(description = "활동 학기") SemesterType semester,
        @Schema(description = "종류") String studyType,
        @Schema(description = "상세설명 노션 링크") String notionLink,
        @Schema(description = "한 줄 소개") String introduction,
        @Schema(description = "멘토 이름") String mentorName,
        @Schema(description = "스터디 요일") DayOfWeek dayOfWeek,
        @Schema(description = "스터디 시작 시간") LocalTime startTime,
        @Schema(description = "스터디 종료 시간") LocalTime endTime,
        @Schema(description = "총 주차수") Long totalWeek,
        @Schema(description = "총 기간") Period period) {
    public static CommonStudyResponse from(Study study) {
        return new CommonStudyResponse(
                study.getId(),
                study.getTitle(),
                study.getAcademicYear(),
                study.getSemesterType(),
                study.getStudyType().getValue(),
                study.getNotionLink(),
                study.getIntroduction(),
                study.getMentor().getName(),
                study.getDayOfWeek(),
                study.getStartTime(),
                study.getEndTime(),
                study.getTotalWeek(),
                study.getPeriod());
    }
}
