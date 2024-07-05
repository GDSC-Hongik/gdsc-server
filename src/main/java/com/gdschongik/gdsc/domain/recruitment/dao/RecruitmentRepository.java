package com.gdschongik.gdsc.domain.recruitment.dao;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    Optional<Recruitment> findByAcademicYearAndSemesterType(Integer academicYear, SemesterType semesterType);

    boolean existsByAcademicYearAndSemesterType(Integer academicYear, SemesterType semesterType);

    List<Recruitment> findByOrderBySemesterPeriodDesc();
}
