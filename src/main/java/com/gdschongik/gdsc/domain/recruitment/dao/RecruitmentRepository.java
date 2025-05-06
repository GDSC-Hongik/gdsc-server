package com.gdschongik.gdsc.domain.recruitment.dao;

import com.gdschongik.gdsc.domain.common.vo.Semester;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long>, RecruitmentCustomRepository {

    boolean existsBySemester(Semester semester);

    List<Recruitment> findByOrderBySemesterPeriodDesc();

    Optional<Recruitment> findBySemester(Semester semester);
}
