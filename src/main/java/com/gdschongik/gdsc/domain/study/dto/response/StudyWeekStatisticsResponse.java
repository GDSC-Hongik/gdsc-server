package com.gdschongik.gdsc.domain.study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record StudyWeekStatisticsResponse(
        @Schema(description = "스터디 주차") Long week,
        @Schema(description = "출석률") Long attendanceRate,
        @Schema(description = "과제 제출률") Long assignmentSubmitRate,
        @Schema(description = "휴강 여부") boolean isCanceledWeek) {

    public static StudyWeekStatisticsResponse openedWeekStatisticsOf(
            Long studyWeek, Long attendanceRate, Long assignmentSubmitRate) {
        return new StudyWeekStatisticsResponse(studyWeek, attendanceRate, assignmentSubmitRate, false);
    }

    public static StudyWeekStatisticsResponse canceledWeekStatisticsFrom(Long studyWeek) {
        return new StudyWeekStatisticsResponse(studyWeek, 0L, 0L, true);
    }
}
