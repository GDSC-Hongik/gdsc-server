package com.gdschongik.gdsc.domain.recruitment.domain;

import static com.gdschongik.gdsc.domain.common.model.SemesterType.*;
import static com.gdschongik.gdsc.global.common.constant.TemporalConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class RecruitmentValidator {

    private final RecruitmentRepository recruitmentRepository;

    public void validateRecruitmentCreate(Integer academicYear, SemesterType semesterType) {
        // 학년도와 학기가 같은 리쿠르팅 중복 검증
        if (recruitmentRepository.existsByAcademicYearAndSemesterType(academicYear, semesterType)) {
            throw new CustomException(RECRUITMENT_OVERLAP);
        }
    }
}
