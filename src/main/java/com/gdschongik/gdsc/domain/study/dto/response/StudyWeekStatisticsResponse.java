package com.gdschongik.gdsc.domain.study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record StudyWeekStatisticsResponse(
        @Schema(description = "스터디 주차") Long studyWeek,
        @Schema(description = "현재 주차 출석률") Long attendanceRate,
        @Schema(description = "현재 주차 과제 제출률") Long assignmentSubmitRate,
        @Schema(description = "스터디 주차 휴강 여부") boolean isCanceledWeek) {

    public static StudyWeekStatisticsResponse createOpenedWeekStatistics(
            Long studyWeek, Long attendanceRate, Long assignmentSubmitRate) {
        return new StudyWeekStatisticsResponse(studyWeek, attendanceRate, assignmentSubmitRate, false);
    }

    public static StudyWeekStatisticsResponse createCanceledWeekStatistics(Long studyWeek) {
        return new StudyWeekStatisticsResponse(studyWeek, 0L, 0L, true);
    }
}
