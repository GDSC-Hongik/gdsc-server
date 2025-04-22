package com.gdschongik.gdsc.domain.studyv2.dto.request;

import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyUpdateCommand;
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
        Integer minAssignmentLength,
        List<StudySessionUpdateDto> studySessions) {
    public record StudySessionUpdateDto(
            Long studySessionId,
            String lessonTitle,
            String description,
            Period lessonPeriod,
            @Nullable String assignmentTitle,
            @Nullable String assignmentDescriptionLink,
            @Nullable Period assignmentPeriod) {}

    public StudyUpdateCommand toCommand() {
        var sessionCommands = studySessions().stream()
                .map(studySession -> new StudyUpdateCommand.Session(
                        studySession.studySessionId(),
                        studySession.lessonTitle(),
                        studySession.description(),
                        studySession.lessonPeriod(),
                        studySession.assignmentTitle(),
                        studySession.assignmentDescriptionLink(),
                        studySession.assignmentPeriod()))
                .toList();

        return new StudyUpdateCommand(
                title,
                description,
                descriptionNotionLink,
                dayOfWeek,
                startTime,
                endTime,
                minAssignmentLength,
                sessionCommands);
    }
}
