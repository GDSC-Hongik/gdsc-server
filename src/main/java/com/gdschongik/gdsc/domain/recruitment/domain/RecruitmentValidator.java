package com.gdschongik.gdsc.domain.recruitment.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;

@DomainService
public class RecruitmentValidator {

    public void validateRecruitmentCreate(boolean isRecruitmentOverlap) {
        // 학년도와 학기가 같은 리쿠르팅이 이미 존재하는 경우
        if (isRecruitmentOverlap) {
            throw new CustomException(RECRUITMENT_OVERLAP);
        }
    }
}
