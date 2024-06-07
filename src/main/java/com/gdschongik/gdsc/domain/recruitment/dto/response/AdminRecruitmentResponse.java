package com.gdschongik.gdsc.domain.recruitment.dto.response;

import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Optional;

public record AdminRecruitmentResponse(
        Long recruitmentId,
        Integer academicYear,
        String semester,
        @Schema(description = "차수") String round,
        String name,
        @Schema(description = "신청기간 시작일") LocalDateTime startDate,
        @Schema(description = "신청기간 종료일") LocalDateTime endDate) {

    public static AdminRecruitmentResponse of(Recruitment recruitment, Integer round) {
        return new AdminRecruitmentResponse(
                recruitment.getId(),
                recruitment.getAcademicYear(),
                recruitment.getSemesterType().getValue(),
                Optional.ofNullable(round).map(r -> String.format("%d차", round)).orElse(null),
                recruitment.getName(),
                recruitment.getPeriod().getStartDate(),
                recruitment.getPeriod().getEndDate());
    }
}
