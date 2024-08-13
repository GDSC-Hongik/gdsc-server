package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.global.util.formatter.SemesterFormatter;
import io.swagger.v3.oas.annotations.media.Schema;

public record MentorStudyResponse(
        Long studyId,
        @Schema(description = "활동 학기") String semester,
        @Schema(description = "이름") String title,
        @Schema(description = "종류") String studyType,
        @Schema(description = "상세설명 노션 링크") String notionLink,
        @Schema(description = "멘토 이름") String mentorName) {

    public static MentorStudyResponse from(Study study) {
        return new MentorStudyResponse(
                study.getId(),
                SemesterFormatter.format(study.getAcademicYear(), study.getSemesterType()),
                study.getTitle(),
                study.getStudyType().getValue(),
                study.getNotionLink(),
                study.getMentor().getName());
    }
}
