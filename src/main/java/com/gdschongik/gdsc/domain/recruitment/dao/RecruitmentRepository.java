package com.gdschongik.gdsc.domain.recruitment.dao;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.Round;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long>, RecruitmentCustomRepository {

    List<Recruitment> findAllByAcademicYearAndSemesterType(Integer academicYear, SemesterType semesterType);

    boolean existsByAcademicYearAndSemesterTypeAndRound(Integer academicYear, SemesterType semesterType, Round round);
}
