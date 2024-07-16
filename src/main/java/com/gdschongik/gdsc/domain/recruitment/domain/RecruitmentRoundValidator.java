package com.gdschongik.gdsc.domain.recruitment.domain;

import static com.gdschongik.gdsc.global.common.constant.TemporalConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDateTime;
import java.util.List;

@DomainService
public class RecruitmentRoundValidator {

    public void validateRecruitmentRoundCreate(
            LocalDateTime startDate,
            LocalDateTime endDate,
            RoundType roundType,
            Recruitment recruitment,
            List<RecruitmentRound> recruitmentRoundsInThisSemester) {
        validatePeriodWithinTwoWeeks(startDate, endDate, recruitment);
        validatePeriodOverlap(recruitmentRoundsInThisSemester, startDate, endDate);
        validateRoundOverlap(recruitmentRoundsInThisSemester, roundType);
        validateRoundOneExist(recruitmentRoundsInThisSemester, roundType);
    }

    private void validatePeriodWithinTwoWeeks(LocalDateTime startDate, LocalDateTime endDate, Recruitment recruitment) {
        LocalDateTime semesterStartDate = recruitment.getSemesterPeriod().getStartDate();

        validateDateTimeWithinTwoWeeks(startDate, semesterStartDate);
        validateDateTimeWithinTwoWeeks(endDate, semesterStartDate);
    }

    private void validateDateTimeWithinTwoWeeks(LocalDateTime dateTime, LocalDateTime semesterStartDate) {
        if (semesterStartDate.minusWeeks(PRE_SEMESTER_TERM).isAfter(dateTime)
                || semesterStartDate.plusWeeks(PRE_SEMESTER_TERM).isBefore(dateTime)) {
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
                && recruitmentRounds.stream().noneMatch(RecruitmentRound::isFirstRound)) {
            throw new CustomException(ROUND_ONE_DOES_NOT_EXIST);
        }
    }
}
