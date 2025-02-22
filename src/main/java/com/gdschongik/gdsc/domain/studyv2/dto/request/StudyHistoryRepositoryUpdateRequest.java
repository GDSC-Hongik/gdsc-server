package com.gdschongik.gdsc.domain.studyv2.dto.request;

import com.gdschongik.gdsc.global.common.constant.RegexConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record StudyHistoryRepositoryUpdateRequest(
        @NotBlank @Pattern(regexp = RegexConstant.GITHUB_REPOSITORY) String repositoryLink) {}
