package com.gdschongik.gdsc.domain.recruitment.dto.request;

import static com.gdschongik.gdsc.global.common.constant.RegexConstant.*;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record RecruitmentCreateRequest(
        @NotBlank @Schema(description = "이름") String name,
        @Future @Schema(description = "모집기간 시작일", pattern = DATETIME) LocalDateTime startDate,
        @Future @Schema(description = "모집기간 종료일", pattern = DATETIME) LocalDateTime endDate) {}
