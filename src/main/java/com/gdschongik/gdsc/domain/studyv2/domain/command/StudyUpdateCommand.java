package com.gdschongik.gdsc.domain.studyv2.domain.command;

import com.gdschongik.gdsc.domain.common.vo.Period;
import jakarta.annotation.Nullable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public record StudyUpdateCommand(
        String title,
        String description,
        String descriptionNotionLink,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        List<Session> studySessions) {

    public record Session(
            Long studySessionId,
            String lessonTitle,
            String description,
            Period lessonPeriod,
            @Nullable String assignmentTitle,
            @Nullable String assignmentDescriptionLink,
            @Nullable Period assignmentPeriod) {}
}
