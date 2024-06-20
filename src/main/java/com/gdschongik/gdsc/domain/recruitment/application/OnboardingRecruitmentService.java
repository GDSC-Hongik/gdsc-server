package com.gdschongik.gdsc.domain.recruitment.application;

import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OnboardingRecruitmentService {

    private final RecruitmentRepository recruitmentRepository;

    // TODO: 모집기간과 별도로 표시기간 사용하여 필터링하도록 변경
    public Recruitment findCurrentRecruitment() {
        return recruitmentRepository.findAll().stream()
                .filter(Recruitment::isOpen)
                .findFirst()
                .orElseThrow();
    }
}
