package com.gdschongik.gdsc.domain.recruitment.dto.request;

import static com.gdschongik.gdsc.global.common.constant.RegexConstant.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record RecruitmentCreateRequest(
        @NotBlank @Schema(description = "이름") String name,
        @Future @Schema(description = "모집기간 시작일", pattern = DATETIME) LocalDateTime startDate,
        @Future @Schema(description = "모집기간 종료일", pattern = DATETIME) LocalDateTime endDate,
        @NotNull(message = "학년도는 null이 될 수 없습니다.") @Schema(description = "학년도", pattern = ACADEMIC_YEAR)
                Integer academicYear,
        @NotNull(message = "학기는 null이 될 수 없습니다.") @Schema(description = "학기") SemesterType semesterType) {}
