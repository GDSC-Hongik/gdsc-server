package com.gdschongik.gdsc.domain.application.domain.dto.request;

import static com.gdschongik.gdsc.global.common.constant.RegexConstant.YEAR;

import com.gdschongik.gdsc.domain.common.model.Semester;
import io.swagger.v3.oas.annotations.media.Schema;

public record ApplicationQueryOption(
        @Schema(description = "년도", pattern = YEAR) Integer year, @Schema(description = "학기") Semester sememster) {}
