package com.gdschongik.gdsc.domain.study.dao;

import com.gdschongik.gdsc.domain.study.domain.StudyAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyAchievementRepository
        extends JpaRepository<StudyAchievement, Long>, StudyAchievementCustomRepository {}
