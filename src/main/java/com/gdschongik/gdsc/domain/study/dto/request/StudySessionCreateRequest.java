package com.gdschongik.gdsc.domain.study.dto.request;

import com.gdschongik.gdsc.domain.study.domain.Difficulty;
import com.gdschongik.gdsc.domain.study.domain.StudyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record StudySessionCreateRequest(
        @NotNull Long studyDetailId,
        @Schema(description = "제목") String title,
        @Schema(description = "설명") String description,
        @Schema(description = "난이도") Difficulty difficulty,
        @Schema(description = "휴강 여부") StudyStatus status) {}
