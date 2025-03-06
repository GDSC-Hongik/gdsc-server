package com.gdschongik.gdsc.domain.studyv2.dto.response;

import com.gdschongik.gdsc.domain.studyv2.domain.StudySessionV2;
import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 응답용 enum

@Getter
@RequiredArgsConstructor
public enum AttendanceStatusResponse {
    ATTENDED("출석"),
    NOT_ATTENDED("미출석"),
    BEFORE_ATTENDANCE("출석전");

    private final String value;

    public static AttendanceStatusResponse of(StudySessionV2 studySessionV2, LocalDate now, boolean isAttended) {
        if (studySessionV2.getLessonPeriod().getStartDate().toLocalDate().isAfter(now)) {
            return BEFORE_ATTENDANCE;
        }

        if (isAttended) {
            return ATTENDED;
        } else {
            return NOT_ATTENDED;
        }
    }
}
