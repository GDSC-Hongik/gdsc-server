package com.gdschongik.gdsc.domain.studyv2.dto.request;

import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.common.vo.Semester;
import com.gdschongik.gdsc.domain.study.domain.StudyType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.DayOfWeek;
import java.time.LocalTime;

public record StudyCreateRequest(
        @NotNull @Positive Long mentorId,
        @NotNull StudyType type,
        @NotNull String title,
        String description,
        String descriptionNotionLink,
        @NotNull Semester semester,
        @NotNull @Positive Integer totalRound,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        @NotNull Period applicationPeriod,
        String discordChannelId,
        String discordRoleId) {}
