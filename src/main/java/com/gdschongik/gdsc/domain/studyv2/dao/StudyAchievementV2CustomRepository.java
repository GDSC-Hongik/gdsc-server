package com.gdschongik.gdsc.domain.studyv2.dao;

import com.gdschongik.gdsc.domain.study.domain.AchievementType;
import java.util.List;

public interface StudyAchievementV2CustomRepository {

    void deleteByStudyAndAchievementTypeAndMemberIds(
            Long studyId, AchievementType achievementType, List<Long> memberIds);

    long countByStudyIdAndAchievementTypeAndStudentIds(
            Long studyId, AchievementType achievementType, List<Long> studentIds);
}
