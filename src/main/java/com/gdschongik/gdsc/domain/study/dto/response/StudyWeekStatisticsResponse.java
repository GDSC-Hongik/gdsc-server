package com.gdschongik.gdsc.domain.study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record StudyWeekStatisticsResponse(
        @Schema(description = "스터디 주차") Long week,
        @Schema(description = "출석률") Long attendanceRate,
        @Schema(description = "과제 제출률") Long assignmentSubmitRate,
        @Schema(description = "과제 휴강 여부") boolean isCanceledAssignment,
        @Schema(description = "스터디 휴강 여부") boolean isCanceledWeek) {

    public static StudyWeekStatisticsResponse openedOf(Long week, Long attendanceRate, Long assignmentSubmitRate) {
        return new StudyWeekStatisticsResponse(week, attendanceRate, assignmentSubmitRate, false, false);
    }

    public static StudyWeekStatisticsResponse emptyOf(Long week, boolean isCanceledAssignment, boolean isCanceledWeek) {
        return new StudyWeekStatisticsResponse(week, 0L, 0L, isCanceledAssignment, isCanceledWeek);
    }

    public static StudyWeekStatisticsResponse canceledWeekFrom(Long week) {
        return StudyWeekStatisticsResponse.emptyOf(week, true, true);
    }

    public static StudyWeekStatisticsResponse canceledAssignmentOf(Long week, Long attendanceRate) {
        return new StudyWeekStatisticsResponse(week, attendanceRate, 0L, true, false);
    }
}
