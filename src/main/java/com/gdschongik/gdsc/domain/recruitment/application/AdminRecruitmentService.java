package com.gdschongik.gdsc.domain.recruitment.application;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentCreateRequest;
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
        LocalDateTime startDate = request.startDate();
        int academicYear = startDate.getYear();
        SemesterType semesterType = SemesterType.from(startDate);

        validatePeriodOverlap(academicYear, semesterType, request.startDate(), request.endDate());

        Recruitment recruitment = Recruitment.createRecruitment(
                request.name(), request.startDate(), request.endDate(), academicYear, semesterType);
        recruitmentRepository.save(recruitment);
    }

    private void validatePeriodOverlap(
            Integer academicYear, SemesterType semesterType, LocalDateTime startDate, LocalDateTime endDate) {
        List<Recruitment> recruitments =
                recruitmentRepository.findAllByAcademicYearAndSemesterType(academicYear, semesterType);

        recruitments.forEach(recruitment -> recruitment.validatePeriodOverlap(startDate, endDate));
    }
}
