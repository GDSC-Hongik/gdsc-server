package com.gdschongik.gdsc.domain.recruitment.application;

import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRoundRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OnboardingRecruitmentService {

    private final RecruitmentRoundRepository recruitmentRoundRepository;

    // TODO: 모집기간과 별도로 표시기간 사용하여 필터링하도록 변경
    public Optional<RecruitmentRound> findCurrentRecruitmentRound() {
        return recruitmentRoundRepository.findAll().stream()
                .filter(RecruitmentRound::isOpen) // isOpen -> isDisplayable
                .findFirst();
    }

    /**
     * 테스트용 강등 API에서 모집 회차가 존재하지 않을 경우에 대해 필요한 메소드입니다.
     */
    public Optional<RecruitmentRound> findCurrentRecruitmentRoundToDemote() {
        return recruitmentRoundRepository.findAll().stream()
                .filter(RecruitmentRound::isOpen)
                .findFirst();
    }
}
