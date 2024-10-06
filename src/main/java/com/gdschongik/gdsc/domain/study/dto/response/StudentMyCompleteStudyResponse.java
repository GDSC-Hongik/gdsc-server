package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.study.domain.AchievementType;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.gdschongik.gdsc.domain.study.domain.StudyHistoryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record StudentMyCompleteStudyResponse(
        Long studyId,
        @Schema(description = "학년도") Integer academicYear,
        @Schema(description = "학기") SemesterType semesterType,
        @Schema(description = "이름") String title,
        @Schema(description = "종류") String studyType,
        @Schema(description = "상세설명 노션 링크") String notionLink,
        @Schema(description = "한 줄 소개") String introduction,
        @Schema(description = "멘토 이름") String mentorName,
        @Schema(description = "총 주차수") Long totalWeek,
        @Schema(description = "수료 여부") StudyHistoryStatus studyHistoryStatus,
        @Schema(description = "우수 스터디원 여부") List<AchievementType> achievements
) {

    public static StudentMyCompleteStudyResponse of(StudyHistory studyHistory, List<AchievementType> achievements) {
        return new StudentMyCompleteStudyResponse(
                studyHistory.getStudy().getId(),
                studyHistory.getStudy().getAcademicYear(),
                studyHistory.getStudy().getSemesterType(),
                studyHistory.getStudy().getTitle(),
                studyHistory.getStudy().getStudyType().getValue(),
                studyHistory.getStudy().getNotionLink(),
                studyHistory.getStudy().getIntroduction(),
                studyHistory.getStudy().getMentor().getName(),
                studyHistory.getStudy().getTotalWeek(),
                studyHistory.getStudyHistoryStatus(),
                achievements
        );
    }
}
