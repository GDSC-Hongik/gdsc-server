package com.gdschongik.gdsc.domain.recruitment.dao;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRoundRepository extends JpaRepository<RecruitmentRound, Long> {

    List<RecruitmentRound> findAllByAcademicYearAndSemesterType(Integer academicYear, SemesterType semesterType);

    // boolean existsByAcademicYearAndSemesterTypeAndRoundType(
    //         Integer academicYear, SemesterType semesterType, RoundType roundType);
}
