package com.gdschongik.gdsc.domain.recruitment.dao;

import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import java.util.List;

public interface RecruitmentCustomRepository {
    List<Recruitment> findAllOrderByStartDate();
}
