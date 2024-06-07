package com.gdschongik.gdsc.domain.recruitment.dto.request;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import io.swagger.v3.oas.annotations.media.Schema;

public record RecruitmentQueryOption(
        @Schema(description = "학년도") Integer academicYear, @Schema(description = "학기") SemesterType semesterType) {}
