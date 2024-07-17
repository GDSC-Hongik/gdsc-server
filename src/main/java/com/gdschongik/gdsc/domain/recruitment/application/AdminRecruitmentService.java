package com.gdschongik.gdsc.domain.recruitment.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRoundRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRoundValidator;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentValidator;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentCreateRequest;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentRoundCreateUpdateRequest;
import com.gdschongik.gdsc.domain.recruitment.dto.response.AdminRecruitmentResponse;
import com.gdschongik.gdsc.domain.recruitment.dto.response.AdminRecruitmentRoundResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminRecruitmentService {

    private final RecruitmentRepository recruitmentRepository;
    private final RecruitmentRoundRepository recruitmentRoundRepository;
    private final RecruitmentValidator recruitmentValidator;
    private final RecruitmentRoundValidator recruitmentRoundValidator;

    @Transactional
    public void createRecruitment(RecruitmentCreateRequest request) {
        boolean isRecruitmentOverlap = recruitmentRepository.existsByAcademicYearAndSemesterType(
                request.academicYear(), request.semesterType());

        recruitmentValidator.validateRecruitmentCreate(isRecruitmentOverlap);

        Recruitment recruitment = Recruitment.createRecruitment(
                request.academicYear(),
                request.semesterType(),
                Money.from(request.fee()),
                Period.createPeriod(request.semesterStartDate(), request.semesterEndDate()));
        recruitmentRepository.save(recruitment);

        log.info("[AdminRecruitmentService] 리쿠르팅 생성: recruitmentId={}", recruitment.getId());
    }

    public List<AdminRecruitmentResponse> getAllRecruitments() {
        List<Recruitment> recruitments = recruitmentRepository.findByOrderBySemesterPeriodDesc();
        return recruitments.stream().map(AdminRecruitmentResponse::from).toList();
    }

    public List<AdminRecruitmentRoundResponse> getAllRecruitmentRounds() {
        List<RecruitmentRound> recruitmentRounds = recruitmentRoundRepository.findAll();
        return recruitmentRounds.stream()
                .map(AdminRecruitmentRoundResponse::from)
                .toList();
    }

    @Transactional
    public void createRecruitmentRound(RecruitmentRoundCreateUpdateRequest request) {
        Recruitment recruitment = recruitmentRepository
                .findByAcademicYearAndSemesterType(request.academicYear(), request.semesterType())
                .orElseThrow(() -> new CustomException(RECRUITMENT_NOT_FOUND));

        List<RecruitmentRound> recruitmentRoundsInThisSemester =
                recruitmentRoundRepository.findAllByAcademicYearAndSemesterType(
                        request.academicYear(), request.semesterType());

        recruitmentRoundValidator.validateRecruitmentRoundCreate(
                request.startDate(),
                request.endDate(),
                request.roundType(),
                recruitment,
                recruitmentRoundsInThisSemester);

        RecruitmentRound recruitmentRound = RecruitmentRound.create(
                request.name(), request.startDate(), request.endDate(), recruitment, request.roundType());
        recruitmentRoundRepository.save(recruitmentRound);

        log.info("[AdminRecruitmentService] 모집회차 생성: recruitmentRoundId={}", recruitmentRound.getId());
    }

    @Transactional
    public void updateRecruitmentRound(Long recruitmentRoundId, RecruitmentRoundUpdateRequest request) {}

    /*
     1. 해당 학기에 리쿠르팅이 존재해야 함.
     2. 해당 학기의 모든 리쿠르팅이 아직 시작되지 않았어야 함.
    */
    public void validateRecruitmentNotStarted(Integer academicYear, SemesterType semesterType) {
        List<RecruitmentRound> recruitmentRounds =
                recruitmentRoundRepository.findAllByAcademicYearAndSemesterType(academicYear, semesterType);

        if (recruitmentRounds.isEmpty()) {
            throw new CustomException(RECRUITMENT_ROUND_NOT_FOUND);
        }

        recruitmentRounds.forEach(RecruitmentRound::validatePeriodNotStarted);
    }

    // /**
    //  * 기존 리쿠르팅 수정하는 경우,
    //  * 자기 자신의 모집기간과 차수는 수정에 성공하면 소멸되므로 무의미함.
    //  * 따라서, 자기 자신은 제외하고 검증.
    //  */
    // private void validatePeriodOverlapExcludingCurrentRecruitment(
    //         Integer academicYear,
    //         SemesterType semesterType,
    //         LocalDateTime startDate,
    //         LocalDateTime endDate,
    //         Long currentRecruitmentId) {
    //     List<RecruitmentRound> recruitmentRounds =
    //             recruitmentRoundRepository.findAllByAcademicYearAndSemesterType(academicYear, semesterType);
    //
    //     recruitmentRounds.stream()
    //             .filter(recruitment -> !recruitment.getId().equals(currentRecruitmentId))
    //             .forEach(r -> r.validatePeriodOverlap(startDate, endDate));
    // }
    //
    // private void validateRoundOverlapExcludingCurrentRecruitment(
    //         Integer academicYear, SemesterType semesterType, RoundType roundType, Long currentRecruitmentId) {
    //     List<RecruitmentRound> recruitmentRounds =
    //             recruitmentRoundRepository.findAllByAcademicYearAndSemesterType(academicYear, semesterType);
    //
    //     recruitmentRounds.stream()
    //             .filter(recruitment -> !recruitment.getId().equals(currentRecruitmentId)
    //                     && recruitment.getRoundType().equals(roundType))
    //             .findAny()
    //             .ifPresent(ignored -> {
    //                 throw new CustomException(RECRUITMENT_ROUND_TYPE_OVERLAP);
    //             });
    // }
}
