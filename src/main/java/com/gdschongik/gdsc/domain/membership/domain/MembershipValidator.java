package com.gdschongik.gdsc.domain.membership.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class MembershipValidator {

    public void validateMembershipSubmit(RecruitmentRound recruitmentRound, boolean isMembershipAlreadySubmitted) {
        // 이미 접수한 멤버십이 있는지 검증
        if (isMembershipAlreadySubmitted) {
            throw new CustomException(MEMBERSHIP_ALREADY_SUBMITTED);
        }

        // 모집 회차가 열려있는지 검증
        if (!recruitmentRound.isOpen()) {
            throw new CustomException(MEMBERSHIP_RECRUITMENT_ROUND_NOT_OPEN);
        }
    }
}
