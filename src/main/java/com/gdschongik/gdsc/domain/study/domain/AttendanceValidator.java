package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDate;
import java.util.Optional;

@DomainService
public class AttendanceValidator {
    public void validateAttendance(
            Optional<StudyHistory> studyHistory,
            StudyDetail studyDetail,
            Study study,
            String attendanceNumber,
            LocalDate date) {
        // 수강하는 스터디인지 검증
        if (studyHistory.isEmpty()) {
            throw new CustomException(STUDY_HISTORY_NOT_FOUND);
        }

        // 출석체크 날짜 검증
        LocalDate attendanceDay = study.getPeriod()
                .getStartDate()
                .toLocalDate()
                .plusDays(7
                        - study.getPeriod().getStartDate().getDayOfWeek().getValue()
                        + study.getDayOfWeek().getValue());
        if (!attendanceDay.equals(date)) {
            throw new CustomException(ATTENDANCE_DATE_INVALID);
        }

        // 출석체크 번호 검증
        if (!studyDetail.getAttendanceNumber().equals(attendanceNumber)) {
            throw new CustomException(ATTENDANCE_NUMBER_MISMATCH);
        }
    }
}
