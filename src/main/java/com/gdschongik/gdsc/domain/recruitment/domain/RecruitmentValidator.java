package com.gdschongik.gdsc.domain.recruitment.domain;

import static com.gdschongik.gdsc.domain.common.model.SemesterType.*;
import static com.gdschongik.gdsc.global.common.constant.TemporalConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDateTime;
import java.time.Month;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class RecruitmentValidator {

    private final RecruitmentRepository recruitmentRepository;

    public void validateRecruitmentCreate(
            LocalDateTime startDate, LocalDateTime endDate, Integer academicYear, SemesterType semesterType) {
        validatePeriodMatchesAcademicYear(startDate, endDate, academicYear);
        validatePeriodMatchesSemesterType(startDate, endDate, semesterType);
        validateRecruitmentOverlap(academicYear, semesterType);
    }

    private void validatePeriodMatchesAcademicYear(
            LocalDateTime startDate, LocalDateTime endDate, Integer academicYear) {
        if (academicYear.equals(startDate.getYear()) && academicYear.equals(endDate.getYear())) {
            return;
        }

        throw new CustomException(RECRUITMENT_PERIOD_MISMATCH_ACADEMIC_YEAR);
    }

    // TODO validateRegularRequirement처럼 로직 변경
    private void validatePeriodMatchesSemesterType(
            LocalDateTime startDate, LocalDateTime endDate, SemesterType semesterType) {
        if (getSemesterTypeByStartDateOrEndDate(startDate).equals(semesterType)
                && getSemesterTypeByStartDateOrEndDate(endDate).equals(semesterType)) {
            return;
        }

        throw new CustomException(RECRUITMENT_PERIOD_MISMATCH_SEMESTER_TYPE);
    }

    private SemesterType getSemesterTypeByStartDateOrEndDate(LocalDateTime dateTime) {
        int year = dateTime.getYear();
        LocalDateTime firstSemesterStartDate = LocalDateTime.of(
                year, FIRST.getStartDate().getMonth(), FIRST.getStartDate().getDayOfMonth(), 0, 0);
        LocalDateTime secondSemesterStartDate = LocalDateTime.of(
                year, SECOND.getStartDate().getMonth(), SECOND.getStartDate().getDayOfMonth(), 0, 0);

        /*
        개강일 기준으로 2주 전까지는 같은 학기로 간주한다.
         */
        if (dateTime.isAfter(firstSemesterStartDate.minusWeeks(PRE_SEMESTER_TERM))
                && dateTime.getMonthValue() < Month.JULY.getValue()) {
            return FIRST;
        }

        if (dateTime.isAfter(secondSemesterStartDate.minusWeeks(PRE_SEMESTER_TERM))) {
            return SECOND;
        }

        throw new CustomException(RECRUITMENT_PERIOD_SEMESTER_TYPE_UNMAPPED);
    }

    private void validateRecruitmentOverlap(Integer academicYear, SemesterType semesterType) {
        if (recruitmentRepository.existsByAcademicYearAndSemesterType(academicYear, semesterType)) {
            throw new CustomException(RECRUITMENT_OVERLAP);
        }
    }
}
