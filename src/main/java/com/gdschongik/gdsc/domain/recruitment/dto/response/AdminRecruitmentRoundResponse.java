package com.gdschongik.gdsc.domain.recruitment.dto.response;

import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record AdminRecruitmentRoundResponse(
        Long recruitmentRoundId,
        @Schema(description = "활동 학기") String semester,
        @Schema(description = "신청 시작일") LocalDateTime startDate,
        @Schema(description = "신청 종료일") LocalDateTime endDate,
        @Schema(description = "모집회차 이름") String name) {

    public static AdminRecruitmentRoundResponse from(RecruitmentRound recruitmentRound) {

        return new AdminRecruitmentRoundResponse(
                recruitmentRound.getId(),
                String.format(
                        "%d-%s",
                        recruitmentRound.getAcademicYear(),
                        recruitmentRound.getSemesterType().getValue()),
                recruitmentRound.getPeriod().getStartDate(),
                recruitmentRound.getPeriod().getEndDate(),
                recruitmentRound.getName());
    }
}
