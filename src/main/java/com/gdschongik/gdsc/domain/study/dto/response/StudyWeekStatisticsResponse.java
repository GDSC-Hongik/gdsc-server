package com.gdschongik.gdsc.domain.study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record StudyWeekStatisticsResponse(
        @Schema(description = "스터디 주차") Long week,
        @Schema(description = "출석률") Long attendanceRate,
        @Schema(description = "과제 제출률") Long assignmentSubmissionRate,
        @Schema(description = "과제 휴강 여부") boolean isAssignmentCanceled,
        @Schema(description = "스터디 휴강 여부") boolean isCanceledWeek) {

    public static StudyWeekStatisticsResponse opened(Long week, Long attendanceRate, Long assignmentSubmissionRate) {
        return new StudyWeekStatisticsResponse(week, attendanceRate, assignmentSubmissionRate, false, false);
    }

    public static StudyWeekStatisticsResponse empty(Long week, boolean isCanceledAssignment, boolean isCanceledWeek) {
        return new StudyWeekStatisticsResponse(week, 0L, 0L, isCanceledAssignment, isCanceledWeek);
    }

    public static StudyWeekStatisticsResponse canceledWeek(Long week) {
        return StudyWeekStatisticsResponse.empty(week, true, true);
    }

    public static StudyWeekStatisticsResponse assignmentCanceled(Long week, Long attendanceRate) {
        return new StudyWeekStatisticsResponse(week, attendanceRate, 0L, true, false);
    }
}
