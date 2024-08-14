package com.gdschongik.gdsc.domain.study.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RepositoryUpdateRequest(@NotBlank String repositoryLink) {}
