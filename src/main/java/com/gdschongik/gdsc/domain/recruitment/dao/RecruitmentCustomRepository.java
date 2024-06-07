package com.gdschongik.gdsc.domain.recruitment.dao;

import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentQueryOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitmentCustomRepository {
    Page<Recruitment> findAllOrderByStartDate(RecruitmentQueryOption queryOption, Pageable pageable);
}
