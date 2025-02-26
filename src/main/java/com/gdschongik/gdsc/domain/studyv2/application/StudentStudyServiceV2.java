package com.gdschongik.gdsc.domain.studyv2.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.STUDY_NOT_FOUND;
import static com.gdschongik.gdsc.global.exception.ErrorCode.STUDY_NOT_UPDATABLE_SESSION_NOT_FOUND;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.dao.AttendanceV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.AttendanceV2;
import com.gdschongik.gdsc.domain.studyv2.domain.AttendanceValidatorV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudySessionV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.dto.request.AttendanceCreateRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentStudyServiceV2 {

    private final MemberUtil memberUtil;
    private final StudyV2Repository studyV2Repository;
    private final AttendanceV2Repository attendanceV2Repository;
    private final StudyHistoryV2Repository studyHistoryV2Repository;
    private final AttendanceValidatorV2 attendanceValidatorV2;

    @Transactional
    public void attend(Long studyId, Long studySessionId, AttendanceCreateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyV2 study = studyV2Repository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));

        StudySessionV2 session = study.getStudySessions().stream()
                .filter(studySession -> Objects.equals(studySession.getId(), studySessionId))
                .findFirst()
                .orElseThrow(() -> new CustomException(STUDY_NOT_UPDATABLE_SESSION_NOT_FOUND));

        boolean isAppliedToStudy = studyHistoryV2Repository.existsByStudentAndStudy(currentMember, study);
        boolean isAlreadyAttended = attendanceV2Repository.existsByStudentAndStudySession(currentMember, session);

        attendanceValidatorV2.validateAttendance(
                session, request.attendanceNumber(), isAppliedToStudy, isAlreadyAttended);

        AttendanceV2 attendance = AttendanceV2.create(currentMember, session);
        attendanceV2Repository.save(attendance);

        log.info(
                "[StudentStudyServiceV2] 스터디 출석체크: attendanceId={}, memberId={}",
                attendance.getId(),
                currentMember.getId());
    }
}
