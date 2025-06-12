package com.gdschongik.gdsc.domain.recruitment.dao;

import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRoundRepository
        extends JpaRepository<RecruitmentRound, Long>, RecruitmentRoundCustomRepository {}
