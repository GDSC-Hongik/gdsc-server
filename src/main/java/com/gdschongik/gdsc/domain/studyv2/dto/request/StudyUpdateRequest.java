package com.gdschongik.gdsc.domain.studyv2.dto.request;

import com.gdschongik.gdsc.domain.common.vo.Period;
import jakarta.annotation.Nullable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public record StudyUpdateRequest(
        String title,
        String description,
        String descriptionNotionLink,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        List<StudySessionUpdateDto> studySessions) {
    public record StudySessionUpdateDto(
            Long studySessionId,
            String title,
            String description,
            Period lessonPeriod,
            @Nullable String assignmentDescriptionLink,
            @Nullable Period assignmentPeriod) {}
}
