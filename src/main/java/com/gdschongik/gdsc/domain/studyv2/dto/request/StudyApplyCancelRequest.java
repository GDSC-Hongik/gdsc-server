package com.gdschongik.gdsc.domain.studyv2.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StudyApplyCancelRequest(@NotNull @Positive Long studyId) {}
