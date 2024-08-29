package com.gdschongik.gdsc.domain.study.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record StudyUpdateRequest(
        @Schema(description = "스터디 소개 페이지 링크") String notionLink,
        @Schema(description = "스터디 한 줄 소개") String introduction,
        List<StudyCurriculumCreateRequest> studyCurriculums) {}
