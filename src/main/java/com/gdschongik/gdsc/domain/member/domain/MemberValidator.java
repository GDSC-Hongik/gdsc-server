package com.gdschongik.gdsc.domain.member.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.util.List;

@DomainService
public class MemberValidator {

    public void validateMemberDemote(List<RecruitmentRound> recruitmentRounds) {

        // 해당 학기에 모집회차가 존재하는지 검증
        if (recruitmentRounds.isEmpty()) {
            throw new CustomException(RECRUITMENT_ROUND_NOT_FOUND);
        }

        // 해당 학기의 모든 모집회차가 아직 시작되지 않았는지 검증
        recruitmentRounds.forEach(RecruitmentRound::validatePeriodNotStarted);
    }
}
