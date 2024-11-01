package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 응답용 enum
@Getter
@RequiredArgsConstructor
public enum AttendanceStatusResponse {
    ATTENDED("출석"),
    NOT_ATTENDED("미출석"),
    BEFORE_ATTENDANCE("출석전"),
    CANCELED("휴강");

    private final String value;

    public static AttendanceStatusResponse of(StudyDetail studyDetail, LocalDate now, boolean isAttended) {
        if (studyDetail.getCurriculum().isCanceled()) {
            return CANCELED;
        }

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
