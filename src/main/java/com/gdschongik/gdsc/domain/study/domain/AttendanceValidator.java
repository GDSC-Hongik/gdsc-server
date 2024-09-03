package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDate;

@DomainService
public class AttendanceValidator {
    public void validateAttendance(
            StudyDetail studyDetail, String attendanceNumber, LocalDate date, boolean isAlreadyAttended) {
        // 출석체크 날짜 검증
        LocalDate attendanceDay = studyDetail.getAttendanceDay();
        if (!attendanceDay.equals(date)) {
            throw new CustomException(ATTENDANCE_DATE_INVALID);
        }

        // 출석체크 번호 검증
        if (!studyDetail.getAttendanceNumber().equals(attendanceNumber)) {
            throw new CustomException(ATTENDANCE_NUMBER_MISMATCH);
        }

        // 출석체크 번호 검증
        if (isAlreadyAttended) {
            throw new CustomException(ALREADY_ATTENDED_STUDY_DETAIL);
        }
    }
}
