package com.gdschongik.gdsc.domain.studyv2.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyAchievementV2;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyAchievementV2Repository
        extends JpaRepository<StudyAchievementV2, Long>, StudyAchievementV2CustomRepository {
    List<StudyAchievementV2> findAllByStudent(Member student);
}
