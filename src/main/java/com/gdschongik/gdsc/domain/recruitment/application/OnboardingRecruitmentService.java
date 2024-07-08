package com.gdschongik.gdsc.domain.recruitment.application;

import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRoundRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OnboardingRecruitmentService {

    private final RecruitmentRoundRepository recruitmentRoundRepository;

    // TODO: 모집기간과 별도로 표시기간 사용하여 필터링하도록 변경
    public RecruitmentRound findCurrentRecruitmentRound() {
        return recruitmentRoundRepository.findAll().stream()
                .filter(RecruitmentRound::isOpen) // isOpen -> isDisplayable
                .findFirst()
                .orElseThrow();
    }
}
