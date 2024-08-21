package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 응답용 enum
@Getter
@AllArgsConstructor
public enum AttendanceStatusResponse {
    ATTENDED("출석"),
    NOT_ATTENDED("미출석"),
    BEFORE_ATTENDANCE("출석전");

    private final String value;

    public static AttendanceStatusResponse of(StudyDetail studyDetail, LocalDate now, boolean isAttended) {
        if (studyDetail.getAttendanceDay().isAfter(now)) {
            return BEFORE_ATTENDANCE;
        }

        if (isAttended) {
            return ATTENDED;
        } else {
            return NOT_ATTENDED;
        }
    }
}
