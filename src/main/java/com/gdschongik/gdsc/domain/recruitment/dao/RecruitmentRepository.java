package com.gdschongik.gdsc.domain.recruitment.dao;

import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
}
