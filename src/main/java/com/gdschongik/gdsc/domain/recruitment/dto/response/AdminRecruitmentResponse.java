package com.gdschongik.gdsc.domain.recruitment.dto.response;

import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.global.util.formatter.SemesterFormatter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

public record AdminRecruitmentResponse(
        Long recruitmentId,
        @Schema(description = "활동 학기") String semester,
        @Schema(description = "학기 시작일") LocalDateTime semesterStartDate,
        @Schema(description = "학기 종료일") LocalDateTime semesterEndDate,
        @Schema(description = "회비") String recruitmentFee) {

    public static AdminRecruitmentResponse from(Recruitment recruitment) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        return new AdminRecruitmentResponse(
                recruitment.getId(),
                SemesterFormatter.format(
                        recruitment.getAcademicYear(),
                        recruitment.getSemesterType().getValue()),
                recruitment.getSemesterPeriod().getStartDate(),
                recruitment.getSemesterPeriod().getEndDate(),
                String.format("%s원", decimalFormat.format(recruitment.getFee().getAmount())));
    }
}
