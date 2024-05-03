package com.gdschongik.gdsc.domain.application.application;

import com.gdschongik.gdsc.domain.application.dao.ApplicationRepository;
import com.gdschongik.gdsc.domain.application.domain.Application;
import com.gdschongik.gdsc.domain.application.domain.dto.request.ApplicationQueryOption;
import com.gdschongik.gdsc.domain.application.domain.dto.response.ApplicationResponse;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public Page<ApplicationResponse> getApplicationsByPaymentStatus(
            ApplicationQueryOption queryOption, RequirementStatus paymentStatus, Pageable pageable) {
        Page<Application> applications = applicationRepository.findAllByPaymentStatus(queryOption, paymentStatus, pageable);
        return applications.map(ApplicationResponse::from);
    }
}
