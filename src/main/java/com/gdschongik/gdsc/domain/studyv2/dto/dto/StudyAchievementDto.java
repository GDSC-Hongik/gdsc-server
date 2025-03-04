package com.gdschongik.gdsc.domain.studyv2.dto.dto;

import com.gdschongik.gdsc.domain.study.domain.AchievementType;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyAchievementV2;

public record StudyAchievementDto(Long studyAchievementId, AchievementType type, Long studentId, Long studyId) {
    public static StudyAchievementDto from(StudyAchievementV2 studyAchievement) {
        return new StudyAchievementDto(
                studyAchievement.getId(),
                studyAchievement.getType(),
                studyAchievement.getStudent().getId(),
                studyAchievement.getStudy().getId());
    }
}
