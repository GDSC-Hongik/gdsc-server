package com.gdschongik.gdsc.domain.recruitment.dao;

import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import java.util.Optional;

public interface RecruitmentCustomRepository {

    Optional<Recruitment> findOpenRecruitment();
}