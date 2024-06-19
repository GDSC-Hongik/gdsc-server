package com.gdschongik.gdsc.domain.recruitment.application;

import static com.gdschongik.gdsc.domain.common.model.SemesterType.*;
import static com.gdschongik.gdsc.global.common.constant.TemporalConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RoundType;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentCreateUpdateRequest;
import com.gdschongik.gdsc.domain.recruitment.dto.response.AdminRecruitmentResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDateTime;
import java.time.Month;
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
    public void createRecruitment(RecruitmentCreateUpdateRequest request) {
        validatePeriodMatchesAcademicYear(request.startDate(), request.endDate(), request.academicYear());
        validatePeriodMatchesSemesterType(request.startDate(), request.endDate(), request.semesterType());
        validatePeriodWithinTwoWeeks(
                request.startDate(), request.endDate(), request.academicYear(), request.semesterType());
        validatePeriodOverlap(request.academicYear(), request.semesterType(), request.startDate(), request.endDate());
        validateRoundOverlap(request.academicYear(), request.semesterType(), request.roundType());

        Recruitment recruitment = Recruitment.createRecruitment(
                request.name(),
                request.startDate(),
                request.endDate(),
                request.academicYear(),
                request.semesterType(),
                request.roundType(),
                Money.from(request.fee()));
        recruitmentRepository.save(recruitment);
    }

    public List<AdminRecruitmentResponse> getAllRecruitments() {
        List<Recruitment> recruitments = recruitmentRepository.findByOrderByPeriodStartDateDesc();
        return recruitments.stream().map(AdminRecruitmentResponse::from).toList();
    }

    @Transactional
    public void updateRecruitment(Long recruitmentId, RecruitmentCreateUpdateRequest request) {
        Recruitment recruitment = recruitmentRepository
                .findById(recruitmentId)
                .orElseThrow(() -> new CustomException(RECRUITMENT_NOT_FOUND));
        validatePeriodMatchesAcademicYear(request.startDate(), request.endDate(), request.academicYear());
        validatePeriodMatchesSemesterType(request.startDate(), request.endDate(), request.semesterType());
        validatePeriodWithinTwoWeeks(
                request.startDate(), request.endDate(), request.academicYear(), request.semesterType());
        validatePeriodOverlapExcludingCurrentRecruitment(
                recruitment.getAcademicYear(),
                recruitment.getSemesterType(),
                request.startDate(),
                request.endDate(),
                recruitment.getId());
        validateRoundOverlapExcludingCurrentRecruitment(
                request.academicYear(), request.semesterType(), request.roundType(), recruitment.getId());

        recruitment.updateRecruitment(
                request.name(),
                request.startDate(),
                request.endDate(),
                request.academicYear(),
                request.semesterType(),
                request.roundType(),
                Money.from(request.fee()));
    }

    /*
     1. 해당 학기에 리쿠르팅이 존재해야 함.
     2. 해당 학기의 모든 리쿠르팅이 아직 시작되지 않았어야 함.
    */
    public void validateRecruitmentNotStarted(Integer academicYear, SemesterType semesterType) {
        List<Recruitment> recruitments =
                recruitmentRepository.findAllByAcademicYearAndSemesterType(academicYear, semesterType);

        if (recruitments.isEmpty()) {
            throw new CustomException(RECRUITMENT_NOT_FOUND);
        }

        recruitments.forEach(Recruitment::validatePeriodNotStarted);
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

    // 새로 생성하는 경우
    private void validatePeriodOverlap(
            Integer academicYear, SemesterType semesterType, LocalDateTime startDate, LocalDateTime endDate) {
        List<Recruitment> recruitments =
                recruitmentRepository.findAllByAcademicYearAndSemesterType(academicYear, semesterType);

        recruitments.forEach(recruitment -> recruitment.validatePeriodOverlap(startDate, endDate));
    }

    private void validateRoundOverlap(Integer academicYear, SemesterType semesterType, RoundType roundType) {
        if (recruitmentRepository.existsByAcademicYearAndSemesterTypeAndRoundType(
                academicYear, semesterType, roundType)) {
            throw new CustomException(RECRUITMENT_ROUND_TYPE_OVERLAP);
        }
    }

    /**
     * 기존 리쿠르팅 수정하는 경우,
     * 자기 자신의 모집기간과 차수는 수정에 성공하면 소멸되므로 무의미함.
     * 따라서, 자기 자신은 제외하고 검증.
     */
    private void validatePeriodOverlapExcludingCurrentRecruitment(
            Integer academicYear,
            SemesterType semesterType,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long currentRecruitmentId) {
        List<Recruitment> recruitments =
                recruitmentRepository.findAllByAcademicYearAndSemesterType(academicYear, semesterType);

        recruitments.stream()
                .filter(recruitment -> !recruitment.getId().equals(currentRecruitmentId))
                .forEach(r -> r.validatePeriodOverlap(startDate, endDate));
    }

    private void validateRoundOverlapExcludingCurrentRecruitment(
            Integer academicYear, SemesterType semesterType, RoundType roundType, Long currentRecruitmentId) {
        List<Recruitment> recruitments =
                recruitmentRepository.findAllByAcademicYearAndSemesterType(academicYear, semesterType);

        recruitments.stream()
                .filter(recruitment -> !recruitment.getId().equals(currentRecruitmentId)
                        && recruitment.getRoundType().equals(roundType))
                .findAny()
                .ifPresent(ignored -> {
                    throw new CustomException(RECRUITMENT_ROUND_TYPE_OVERLAP);
                });
    }
}
