package com.gdschongik.gdsc.domain.recruitment.dao;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RoundType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    List<Recruitment> findAllByAcademicYearAndSemesterType(Integer academicYear, SemesterType semesterType);

    List<Recruitment> findByOrderByPeriodStartDateDesc();

    boolean existsByAcademicYearAndSemesterTypeAndRoundType(
            Integer academicYear, SemesterType semesterType, RoundType roundType);
}
