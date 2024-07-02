package com.gdschongik.gdsc.domain.study.dto.request;

import static com.gdschongik.gdsc.global.common.constant.RegexConstant.ACADEMIC_YEAR;
import static com.gdschongik.gdsc.global.common.constant.RegexConstant.DATE;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.study.domain.StudyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.DayOfWeek;
import java.time.LocalDate;

public record StudyCreateRequest(
        @NotNull(message = "스터디 멘토 ID는 null이 될 수 없습니다.") @Schema(description = "스터디 멘토 ID") Long mentorId,
        @NotNull(message = "학년도는 null이 될 수 없습니다.") @Schema(description = "학년도", pattern = ACADEMIC_YEAR)
                Integer academicYear,
        @NotNull(message = "학기는 null이 될 수 없습니다.") @Schema(description = "학기") SemesterType semesterType,
        @NotNull(message = "신청기간 시작일은 null이 될 수 없습니다.") @Schema(description = "신청기간 시작일", pattern = DATE)
                LocalDate applicationStartDate,
        @Future @NotNull(message = "신청기간 종료일은 null이 될 수 없습니다.") @Schema(description = "신청기간 종료일", pattern = DATE)
                LocalDate applicationEndDate,
        @Positive @NotNull(message = "총 주차수는 null이 될 수 없습니다.") @Schema(description = "총 주차수") Long totalWeek,
        @Future @NotNull(message = "스터디 시작일은 null이 될 수 없습니다.") @Schema(description = "스터디 시작일", pattern = DATE)
                LocalDate startDate,
        @NotNull(message = "스터디 요일은 null이 될 수 없습니다.") @Schema(description = "스터디 요일", implementation = DayOfWeek.class)
                DayOfWeek dayOfWeek,
        @NotNull(message = "스터디 타입은 null이 될 수 없습니다.") @Schema(description = "스터디 타입", implementation = StudyType.class)
                StudyType studyType) {}
