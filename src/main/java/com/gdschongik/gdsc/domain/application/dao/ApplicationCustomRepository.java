package com.gdschongik.gdsc.domain.application.dao;

import com.gdschongik.gdsc.domain.application.domain.Application;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationCustomRepository {
    Page<Application> findAllByPaymentStatus(RequirementStatus paymentStatus, Pageable pageable);
}
