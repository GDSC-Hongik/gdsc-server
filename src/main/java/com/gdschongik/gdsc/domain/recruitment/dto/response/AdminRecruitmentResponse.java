package com.gdschongik.gdsc.domain.recruitment.dto.response;

import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record AdminRecruitmentResponse(
        Long recruitmentId,
        @Schema(description = "활동 학기") String semester,
        @Schema(description = "차수") String round,
        String name,
        @Schema(description = "신청기간 시작일") LocalDateTime startDate,
        @Schema(description = "신청기간 종료일") LocalDateTime endDate) {

    public static AdminRecruitmentResponse from(Recruitment recruitment) {
        return new AdminRecruitmentResponse(
                recruitment.getId(),
                String.format(
                        "%d-%s",
                        recruitment.getAcademicYear(),
                        recruitment.getSemesterType().getValue()),
                recruitment.getRoundType().getValue(),
                recruitment.getName(),
                recruitment.getPeriod().getStartDate(),
                recruitment.getPeriod().getEndDate());
    }
}
