package com.gdschongik.gdsc.domain.study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record StudyStatisticsResponse(
        @Schema(description = "스터디 전체 수강생 수") Long totalStudentCount,
        @Schema(description = "스터디 수료 수강생 수") Long completeStudentCount,
        @Schema(description = "출석률 평균") Long averageAttendanceRate,
        @Schema(description = "과제 제출률 평균") Long averageAssignmentSubmitRate,
        @Schema(description = "스터디 수료율") Long studyCompleteRate,
        @Schema(description = "주차별 출석 및 과제 통계") List<StudyWeekStatisticsResponse> studyWeekStatisticsResponses) {

    public static StudyStatisticsResponse of(
            Long totalStudentCount,
            Long completeStudentCount,
            Long averageAttendanceRate,
            Long averageAssignmentSubmitRate,
            List<StudyWeekStatisticsResponse> studyWeekStatisticsResponses) {
        return new StudyStatisticsResponse(
                totalStudentCount,
                completeStudentCount,
                averageAttendanceRate,
                averageAssignmentSubmitRate,
                Math.round(completeStudentCount / (double) totalStudentCount * 100),
                studyWeekStatisticsResponses);
    }
}
