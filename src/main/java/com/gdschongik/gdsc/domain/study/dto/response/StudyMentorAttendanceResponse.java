package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record StudyMentorAttendanceResponse(
        Long studyDetailId,
        @Schema(description = "주차수") Long week,
        @Schema(description = "마감시각") LocalDateTime deadLine,
        @Schema(description = "출석번호") String attendanceNumber) {
    public static StudyMentorAttendanceResponse from(StudyDetail studyDetail) {
        return new StudyMentorAttendanceResponse(
                studyDetail.getId(),
                studyDetail.getWeek(),
                studyDetail.getAttendanceDay().atTime(23, 59, 59),
                studyDetail.getAttendanceNumber());
    }
}
