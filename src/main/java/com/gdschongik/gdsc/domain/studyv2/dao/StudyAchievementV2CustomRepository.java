package com.gdschongik.gdsc.domain.studyv2.dao;

import com.gdschongik.gdsc.domain.study.domain.AchievementType;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyAchievementV2;
import java.util.List;

public interface StudyAchievementV2CustomRepository {

    List<StudyAchievementV2> findByStudyIdAndMemberIds(Long studyId, List<Long> memberIds);

    void deleteByStudyAndAchievementTypeAndMemberIds(
            Long studyId, AchievementType achievementType, List<Long> memberIds);

    long countByStudyIdAndAchievementTypeAndStudentIds(
            Long studyId, AchievementType achievementType, List<Long> studentIds);
}
