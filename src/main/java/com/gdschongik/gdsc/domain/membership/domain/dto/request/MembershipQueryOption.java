package com.gdschongik.gdsc.domain.membership.domain.dto.request;

import static com.gdschongik.gdsc.global.common.constant.RegexConstant.YEAR;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import io.swagger.v3.oas.annotations.media.Schema;

public record MembershipQueryOption(
        @Schema(description = "년도", pattern = YEAR) Integer year,
        @Schema(description = "학기") SemesterType semesterType) {}
