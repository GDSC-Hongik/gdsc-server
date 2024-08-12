package com.gdschongik.gdsc.domain.study.dto.response;

import static com.gdschongik.gdsc.global.util.formatter.StudyFormatter.*;

import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.global.util.formatter.SemesterFormatter;
import io.swagger.v3.oas.annotations.media.Schema;

public record CommonStudyResponse(
        Long studyId,
        @Schema(description = "이름") String title,
        @Schema(description = "활동 학기") String semester,
        @Schema(description = "종류") String studyType,
        @Schema(description = "상세설명 노션 링크") String notionLink,
        @Schema(description = "한 줄 소개") String introduction,
        @Schema(description = "멘토 이름") String mentorName,
        @Schema(description = "스터디 시간") String schedule,
        @Schema(description = "총 주차수") String totalWeek,
        @Schema(description = "총 기간") String period) {
    public static CommonStudyResponse from(Study study) {
        return new CommonStudyResponse(
                study.getId(),
                SemesterFormatter.format(study.getAcademicYear(), study.getSemesterType()),
                study.getTitle(),
                study.getStudyType().getValue(),
                study.getNotionLink(),
                study.getIntroduction(),
                study.getMentor().getName(),
                getSchedule(study.getDayOfWeek(), study.getStartTime(), study.getEndTime()),
                study.getTotalWeek().toString() + "주 코스",
                getPeriod(study.getPeriod()));
    }
}
