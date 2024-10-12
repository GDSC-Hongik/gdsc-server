package com.gdschongik.gdsc.domain.recruitment.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRoundRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRoundValidator;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentValidator;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentCreateRequest;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentRoundCreateRequest;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentRoundUpdateRequest;
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
                request.feeName(),
                Period.of(request.semesterStartDate(), request.semesterEndDate()));
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
    public void createRecruitmentRound(RecruitmentRoundCreateRequest request) {
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
                request.name(), Period.of(request.startDate(), request.endDate()), recruitment, request.roundType());
        recruitmentRoundRepository.save(recruitmentRound);

        log.info("[AdminRecruitmentService] 모집회차 생성: recruitmentRoundId={}", recruitmentRound.getId());
    }

    @Transactional
    public void updateRecruitmentRound(Long recruitmentRoundId, RecruitmentRoundUpdateRequest request) {
        List<RecruitmentRound> recruitmentRounds = recruitmentRoundRepository.findAllByAcademicYearAndSemesterType(
                request.academicYear(), request.semesterType());

        RecruitmentRound recruitmentRound = recruitmentRounds.stream()
                .filter(r -> r.getId().equals(recruitmentRoundId))
                .findAny()
                .orElseThrow(() -> new CustomException(RECRUITMENT_ROUND_NOT_FOUND));

        recruitmentRounds.remove(recruitmentRound);

        recruitmentRoundValidator.validateRecruitmentRoundUpdate(
                request.startDate(), request.endDate(), request.roundType(), recruitmentRound, recruitmentRounds);

        recruitmentRound.updateRecruitmentRound(
                request.name(), Period.of(request.startDate(), request.endDate()), request.roundType());

        log.info("[AdminRecruitmentService] 모집회차 수정: recruitmentRoundId={}", recruitmentRoundId);
    }
}
