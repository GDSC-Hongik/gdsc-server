package com.gdschongik.gdsc.domain.study.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.StudyAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyAchievementRepository extends JpaRepository<StudyAchievement, Long> {

    List<StudyAchievement> findAllByStudent(Member student);
}
