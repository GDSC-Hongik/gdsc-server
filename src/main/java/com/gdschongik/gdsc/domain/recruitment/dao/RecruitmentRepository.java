package com.gdschongik.gdsc.domain.recruitment.dao;

import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    // boolean existsByAcademicYearAndSemesterType(Integer academicYear, SemesterType semesterType);

    List<Recruitment> findByOrderBySemesterPeriodDesc();
}
