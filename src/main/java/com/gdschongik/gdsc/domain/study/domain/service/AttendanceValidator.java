package com.gdschongik.gdsc.domain.study.domain.service;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDate;

@DomainService
public class AttendanceValidator {

    public void validateAttendance(
            StudyDetail studyDetail,
            String attendanceNumber,
            LocalDate date,
            boolean isAlreadyAttended,
            boolean isAppliedToStudy) {
        // 출석체크 날짜 검증
        LocalDate attendanceDay = studyDetail.getAttendanceDay();
        if (!attendanceDay.equals(date)) {
            throw new CustomException(STUDY_SESSION_NOT_ATTENDABLE_DATE);
        }

        // 출석체크 번호 검증
        if (!studyDetail.getAttendanceNumber().equals(attendanceNumber)) {
            throw new CustomException(ATTENDANCE_NUMBER_MISMATCH);
        }

        // 출석체크 번호 검증
        if (isAlreadyAttended) {
            throw new CustomException(STUDY_DETAIL_ALREADY_ATTENDED);
        }

        // 스터디 신청 여부 검증
        if (!isAppliedToStudy) {
            throw new CustomException(STUDY_HISTORY_NOT_FOUND);
        }
    }
}
