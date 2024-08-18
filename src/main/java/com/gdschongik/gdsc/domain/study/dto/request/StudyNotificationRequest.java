package com.gdschongik.gdsc.domain.study.dto.request;

import static com.gdschongik.gdsc.global.common.constant.RegexConstant.ATTENDANCE_NUMBER;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record StudyNotificationRequest(
        @NotBlank(message = "공지제목이 비었습니다.") @Schema(description = "공지제목") String title,
        @Schema(description = "공지링크") String link) {}
