package com.gdschongik.gdsc.domain.recruitment.domain;

import static com.gdschongik.gdsc.domain.common.model.SemesterType.*;
import static com.gdschongik.gdsc.global.common.constant.TemporalConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@DomainService
public class RecruitmentRoundValidator {

    public void validateRecruitmentRoundCreate(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer academicYear,
            SemesterType semesterType,
            RoundType roundType,
            List<RecruitmentRound> recruitmentRoundsInThisSemester) {
        validatePeriodMatchesAcademicYear(startDate, endDate, academicYear);
        validatePeriodMatchesSemesterType(startDate, endDate, semesterType);
        validatePeriodWithinTwoWeeks(startDate, endDate, academicYear, semesterType);
        validatePeriodOverlap(recruitmentRoundsInThisSemester, startDate, endDate);
        validateRoundOverlap(recruitmentRoundsInThisSemester, roundType);
        validateRoundOneExist(recruitmentRoundsInThisSemester, roundType);
    }

    // TODO validateRegularRequirement처럼 로직 변경
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

    private void validatePeriodWithinTwoWeeks(
            LocalDateTime startDate, LocalDateTime endDate, Integer academicYear, SemesterType semesterType) {
        LocalDateTime semesterStartDate = LocalDateTime.of(
                academicYear,
                semesterType.getStartDate().getMonth(),
                semesterType.getStartDate().getDayOfMonth(),
                0,
                0);

        if (semesterStartDate.minusWeeks(PRE_SEMESTER_TERM).isAfter(startDate)
                || semesterStartDate.plusWeeks(PRE_SEMESTER_TERM).isBefore(startDate)) {
            throw new CustomException(RECRUITMENT_PERIOD_NOT_WITHIN_TWO_WEEKS);
        }

        if (semesterStartDate.minusWeeks(PRE_SEMESTER_TERM).isAfter(endDate)
                || semesterStartDate.plusWeeks(PRE_SEMESTER_TERM).isBefore(endDate)) {
            throw new CustomException(RECRUITMENT_PERIOD_NOT_WITHIN_TWO_WEEKS);
        }
    }

    private void validatePeriodOverlap(
            List<RecruitmentRound> recruitmentRounds, LocalDateTime startDate, LocalDateTime endDate) {
        recruitmentRounds.forEach(recruitmentRound -> recruitmentRound.validatePeriodOverlap(startDate, endDate));
    }

    // 학년도, 학기, 모집회차가 모두 같은 경우
    private void validateRoundOverlap(List<RecruitmentRound> recruitmentRounds, RoundType roundType) {
        recruitmentRounds.stream()
                .filter(recruitmentRound -> recruitmentRound.getRoundType().equals(roundType))
                .findAny()
                .ifPresent(ignored -> {
                    throw new CustomException(RECRUITMENT_ROUND_TYPE_OVERLAP);
                });
    }

    // 1차 모집이 없는데 2차 모집을 생성하려고 하는 경우
    private void validateRoundOneExist(List<RecruitmentRound> recruitmentRounds, RoundType roundType) {
        if (roundType.equals(RoundType.SECOND)
                && recruitmentRounds.stream()
                        .noneMatch(recruitmentRound ->
                                recruitmentRound.getRoundType().equals(RoundType.FIRST))) {
            throw new CustomException(ROUND_ONE_DOES_NOT_EXIST);
        }
    }
}