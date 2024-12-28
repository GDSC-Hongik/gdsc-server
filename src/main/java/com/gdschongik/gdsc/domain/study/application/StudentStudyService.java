package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.AttendanceRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.*;
import com.gdschongik.gdsc.domain.study.domain.Attendance;
import com.gdschongik.gdsc.domain.study.domain.AttendanceValidator;
import com.gdschongik.gdsc.domain.study.dto.request.StudyAttendCreateRequest;
import com.gdschongik.gdsc.domain.study.dto.response.StudentMyCurrentStudyResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyApplicableResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentStudyService {

    private final MemberUtil memberUtil;
    private final StudyRepository studyRepository;
    private final StudyDetailRepository studyDetailRepository;
    private final StudyHistoryRepository studyHistoryRepository;
    private final StudyHistoryValidator studyHistoryValidator;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceValidator attendanceValidator;

    public StudyApplicableResponse getAllApplicableStudies() {
        Member currentMember = memberUtil.getCurrentMember();
        List<StudyHistory> studyHistories = studyHistoryRepository.findAllByStudent(currentMember);
        Optional<Study> appliedStudy = studyHistories.stream()
                .filter(studyHistory -> studyHistory.isWithinApplicationAndCourse(LocalDateTime.now()))
                .map(StudyHistory::getStudy)
                .findFirst();
        List<StudyResponse> studyResponses = studyRepository.findAll().stream()
                .filter(Study::isApplicable)
                .map(StudyResponse::from)
                .toList();

        return StudyApplicableResponse.of(appliedStudy.orElse(null), studyResponses);
    }

    @Transactional
    public void applyStudy(Long studyId) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        Member currentMember = memberUtil.getCurrentMember();

        List<StudyHistory> currentMemberStudyHistories = studyHistoryRepository.findAllByStudent(currentMember);

        studyHistoryValidator.validateApplyStudy(study, currentMemberStudyHistories, LocalDateTime.now());

        StudyHistory studyHistory = StudyHistory.create(currentMember, study);
        studyHistoryRepository.save(studyHistory);

        log.info("[StudyService] 스터디 수강신청: studyHistoryId={}", studyHistory.getId());
    }

    @Transactional
    public void cancelStudyApply(Long studyId) {
        // TODO: 통합 테스트 통해 수강철회 관련 이벤트 처리 확인
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        Member currentMember = memberUtil.getCurrentMember();

        studyHistoryValidator.validateCancelStudyApply(study);

        StudyHistory studyHistory = studyHistoryRepository
                .findByStudentAndStudy(currentMember, study)
                .orElseThrow(() -> new CustomException(STUDY_HISTORY_NOT_FOUND));
        studyHistoryRepository.delete(studyHistory);

        log.info("[StudyService] 스터디 수강신청 취소: appliedStudyId={}, memberId={}", study.getId(), currentMember.getId());
    }

    @Transactional
    public void attend(Long studyDetailId, StudyAttendCreateRequest request) {
        final StudyDetail studyDetail = studyDetailRepository
                .findById(studyDetailId)
                .orElseThrow(() -> new CustomException(STUDY_DETAIL_NOT_FOUND));
        final Member currentMember = memberUtil.getCurrentMember();
        final Study study = studyDetail.getStudy();
        final boolean isAlreadyAttended =
                attendanceRepository.existsByStudentIdAndStudyDetailId(currentMember.getId(), studyDetailId);
        boolean isAppliedToStudy = studyHistoryRepository.existsByStudentAndStudy(currentMember, study);

        attendanceValidator.validateAttendance(
                studyDetail, request.attendanceNumber(), LocalDate.now(), isAlreadyAttended, isAppliedToStudy);

        Attendance attendance = Attendance.of(currentMember, studyDetail);
        attendanceRepository.save(attendance);

        log.info("[StudyService] 스터디 출석: attendanceId={}, memberId={}", attendance.getId(), currentMember.getId());
    }

    @Transactional(readOnly = true)
    public StudentMyCurrentStudyResponse getMyCurrentStudy() {
        Member currentMember = memberUtil.getCurrentMember();
        StudyHistory currentStudyhistory = studyHistoryRepository.findAllByStudent(currentMember).stream()
                .filter(studyHistory -> studyHistory.isWithinApplicationAndCourse(LocalDateTime.now()))
                .findFirst()
                .orElse(null);
        return StudentMyCurrentStudyResponse.from(currentStudyhistory);
    }
}
