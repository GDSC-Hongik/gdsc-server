package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record StudyResponse(
        Long studyId,
        @Schema(description = "이름") String title,
        @Schema(description = "종류") String studyType,
        @Schema(description = "상세설명 노션 링크") String notionLink,
        @Schema(description = "한 줄 소개") String introduction,
        @Schema(description = "멘토 이름") String mentorName,
        @Schema(description = "스터디 시간") String schedule,
        @Schema(description = "총 주차수") String totalWeek,
        @Schema(description = "개강일") String openingDate) {

    public static StudyResponse from(Study study) {
        return new StudyResponse(
                study.getId(),
                study.getTitle(),
                study.getStudyType().getValue(),
                study.getNotionLink(),
                study.getIntroduction(),
                study.getMentor().getName(),
                getSchedule(study.getDayOfWeek(), study.getStartTime()),
                study.getTotalWeek().toString() + "주 코스",
                DateTimeFormatter.ofPattern("MM.dd").format(study.getPeriod().getStartDate()) + " 개강");
    }

    private static String getSchedule(DayOfWeek dayOfWeek, LocalTime startTime) {
        return getKoreanDayOfWeek(dayOfWeek) + startTime.format(DateTimeFormatter.ofPattern("HH")) + "시";
    }

    private static String getKoreanDayOfWeek(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> "월";
            case TUESDAY -> "화";
            case WEDNESDAY -> "수";
            case THURSDAY -> "목";
            case FRIDAY -> "금";
            case SATURDAY -> "토";
            case SUNDAY -> "일";
            default -> "";
        };
    }
}
