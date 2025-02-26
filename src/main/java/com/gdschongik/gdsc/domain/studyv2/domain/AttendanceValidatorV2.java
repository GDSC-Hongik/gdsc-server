package com.gdschongik.gdsc.domain.studyv2.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.ATTENDANCE_NUMBER_MISMATCH;
import static com.gdschongik.gdsc.global.exception.ErrorCode.ATTENDANCE_PERIOD_INVALID;
import static com.gdschongik.gdsc.global.exception.ErrorCode.STUDY_DETAIL_ALREADY_ATTENDED;
import static com.gdschongik.gdsc.global.exception.ErrorCode.STUDY_HISTORY_NOT_FOUND;

import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;

@DomainService
public class AttendanceValidatorV2 {

    public void validateAttendance(
            StudySessionV2 studySession, String attendanceNumber, boolean isAppliedToStudy, boolean isAlreadyAttended) {
        // 출석체크 기간 검증
        if (!studySession.getLessonPeriod().isOpen()) {
            throw new CustomException(ATTENDANCE_PERIOD_INVALID);
        }

        // 출석체크 번호 검증
        if (!studySession.getLessonAttendanceNumber().equals(attendanceNumber)) {
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
