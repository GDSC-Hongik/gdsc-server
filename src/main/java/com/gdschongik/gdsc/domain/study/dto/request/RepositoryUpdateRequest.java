package com.gdschongik.gdsc.domain.study.dto.request;

import com.gdschongik.gdsc.global.common.constant.RegexConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RepositoryUpdateRequest(
        @NotBlank @Pattern(regexp = RegexConstant.GITHUB_REPOSITORY) String repositoryLink) {}
