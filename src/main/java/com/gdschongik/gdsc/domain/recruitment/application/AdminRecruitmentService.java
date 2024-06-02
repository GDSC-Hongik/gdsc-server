package com.gdschongik.gdsc.domain.recruitment.application;

import static com.gdschongik.gdsc.global.common.constant.TemporalConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentCreateRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminRecruitmentService {

    private final RecruitmentRepository recruitmentRepository;

    @Transactional
    public void createRecruitment(RecruitmentCreateRequest request) {
        validatePeriodMatchesAcademicYear(request.startDate(), request.endDate(), request.academicYear());
        validatePeriodMatchesSemesterType(request.startDate(), request.endDate(), request.semesterType());
        validatePeriodWithinTwoWeeks(
                request.startDate(), request.endDate(), request.academicYear(), request.semesterType());
        validatePeriodOverlap(request.academicYear(), request.semesterType(), request.startDate(), request.endDate());

        Recruitment recruitment = Recruitment.createRecruitment(
                request.name(), request.startDate(), request.endDate(), request.academicYear(), request.semesterType());
        recruitmentRepository.save(recruitment);
        // todo: recruitment 모집 시작 직전에 멤버 역할 수정하는 로직 필요.
    }

    private void validatePeriodMatchesAcademicYear(
            LocalDateTime startDate, LocalDateTime endDate, Integer academicYear) {
        if (academicYear.equals(startDate.getYear()) && academicYear.equals(endDate.getYear())) {
            return;
        }

        throw new CustomException(RECRUITMENT_PERIOD_MISMATCH_ACADEMIC_YEAR);
    }

    private void validatePeriodMatchesSemesterType(
            LocalDateTime startDate, LocalDateTime endDate, SemesterType semesterType) {
        if (SemesterType.from(startDate).equals(semesterType)
                && SemesterType.from(endDate).equals(semesterType)) {
            return;
        }

        throw new CustomException(RECRUITMENT_PERIOD_MISMATCH_SEMESTER_TYPE);
    }

    private void validatePeriodWithinTwoWeeks(
            LocalDateTime startDate, LocalDateTime endDate, Integer academicYear, SemesterType semesterType) {
        LocalDateTime semesterStartDate = LocalDateTime.of(
                academicYear,
                semesterType.getStartDate().getMonth(),
                semesterType.getStartDate().getDayOfMonth(),
                0,
                0);

        if (semesterStartDate.minusWeeks(TWO_WEEKS).isAfter(startDate)
                || semesterStartDate.plusWeeks(TWO_WEEKS).isBefore(startDate)) {
            throw new CustomException(RECRUITMENT_PERIOD_NOT_WITHIN_TWO_WEEKS);
        }

        if (semesterStartDate.minusWeeks(TWO_WEEKS).isAfter(endDate)
                || semesterStartDate.plusWeeks(TWO_WEEKS).isBefore(endDate)) {
            throw new CustomException(RECRUITMENT_PERIOD_NOT_WITHIN_TWO_WEEKS);
        }
    }

    private void validatePeriodOverlap(
            Integer academicYear, SemesterType semesterType, LocalDateTime startDate, LocalDateTime endDate) {
        List<Recruitment> recruitments =
                recruitmentRepository.findAllByAcademicYearAndSemesterType(academicYear, semesterType);

        recruitments.forEach(recruitment -> recruitment.validatePeriodOverlap(startDate, endDate));
    }
}
