package com.gdschongik.gdsc.domain.study.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record AssignmentCreateRequest(
        @NotBlank @Schema(description = "과제 제목") String title,
        @NotBlank @Schema(description = "과제 명세 노션 링크") String descriptionNotionLink,
        @Future @Schema(description = "과제 마감일") LocalDateTime deadLine) {}
