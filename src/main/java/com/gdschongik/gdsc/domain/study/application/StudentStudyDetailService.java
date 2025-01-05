package com.gdschongik.gdsc.domain.study.application;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.AssignmentHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.AttendanceRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyHistoryRepository;
import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import com.gdschongik.gdsc.domain.study.domain.Attendance;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.gdschongik.gdsc.domain.study.dto.response.*;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentStudyDetailService {

    private final MemberUtil memberUtil;
    private final StudyHistoryRepository studyHistoryRepository;
    private final AssignmentHistoryRepository assignmentHistoryRepository;
    private final StudyDetailRepository studyDetailRepository;
    private final AttendanceRepository attendanceRepository;

    @Transactional(readOnly = true)
    public AssignmentDashboardResponse getSubmittableAssignments(Long studyId) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyHistory studyHistory = studyHistoryRepository
                .findByStudentAndStudyId(currentMember, studyId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_HISTORY_NOT_FOUND));
        List<AssignmentHistory> assignmentHistories =
                assignmentHistoryRepository.findAssignmentHistoriesByStudentAndStudyId(currentMember, studyId);

        LocalDateTime now = LocalDateTime.now();
        List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyIdOrderByWeekAsc(studyId).stream()
                .filter(studyDetail ->
                        studyDetail.getAssignment().isOpen() && studyDetail.isAssignmentDeadlineRemaining(now))
                .toList();

        boolean isAnySubmitted = assignmentHistories.stream().anyMatch(AssignmentHistory::isSubmitted);
        List<AssignmentSubmittableDto> submittableAssignments = studyDetails.stream()
                .map(studyDetail -> AssignmentSubmittableDto.of(
                        studyDetail, getSubmittedAssignment(assignmentHistories, studyDetail)))
                .toList();

        return AssignmentDashboardResponse.of(studyHistory.getRepositoryLink(), isAnySubmitted, submittableAssignments);
    }

    @Transactional(readOnly = true)
    public List<StudyTaskResponse> getStudyTodoList(Long studyId) {
        Member member = memberUtil.getCurrentMember();
        final List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyIdOrderByWeekAsc(studyId);
        final List<AssignmentHistory> assignmentHistories =
                assignmentHistoryRepository.findAssignmentHistoriesByStudentAndStudyId(member, studyId);
        final List<Attendance> attendances = attendanceRepository.findByMemberAndStudyId(member, studyId);

        LocalDateTime now = LocalDateTime.now();
        List<StudyTaskResponse> response = new ArrayList<>();
        // 출석체크 정보 (개설 상태이고, 오늘이 출석체크날짜인 것)
        studyDetails.stream()
                .filter(studyDetail -> studyDetail.getCurriculum().isOpen()
                        && studyDetail.getAttendanceDay().equals(now.toLocalDate()))
                .forEach(studyDetail -> response.add(StudyTaskResponse.createAttendanceType(
                        studyDetail, now.toLocalDate(), isAttended(attendances, studyDetail))));

        // 과제 정보 (오늘이 과제 제출 기간에 포함된 과제 정보)
        studyDetails.stream()
                .filter(studyDetail -> studyDetail.getAssignment().isOpen()
                        && studyDetail.getAssignment().isDeadlineRemaining(now))
                .forEach(studyDetail -> response.add(StudyTaskResponse.createAssignmentType(
                        studyDetail, getSubmittedAssignment(assignmentHistories, studyDetail))));
        return response;
    }

    public List<StudyStudentCurriculumResponse> getStudyCurriculums(Long studyId) {
        Member member = memberUtil.getCurrentMember();
        final List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyIdOrderByWeekAsc(studyId);
        final List<AssignmentHistory> assignmentHistories =
                assignmentHistoryRepository.findAssignmentHistoriesByStudentAndStudyId(member, studyId);
        final List<Attendance> attendances = attendanceRepository.findByMemberAndStudyId(member, studyId);
        LocalDateTime now = LocalDateTime.now();

        return studyDetails.stream()
                .map(studyDetail -> StudyStudentCurriculumResponse.of(
                        studyDetail,
                        getSubmittedAssignment(assignmentHistories, studyDetail),
                        isAttended(attendances, studyDetail),
                        now))
                .toList();
    }

    private AssignmentHistory getSubmittedAssignment(
            List<AssignmentHistory> assignmentHistories, StudyDetail studyDetail) {
        return assignmentHistories.stream()
                .filter(assignmentHistory ->
                        assignmentHistory.getStudyDetail().getId().equals(studyDetail.getId()))
                .findFirst()
                .orElse(null);
    }

    private boolean isAttended(List<Attendance> attendances, StudyDetail studyDetail) {
        return attendances.stream()
                .anyMatch(attendance -> attendance.getStudyDetail().getId().equals(studyDetail.getId()));
    }

    @Transactional(readOnly = true)
    public List<AssignmentHistoryStatusResponse> getUpcomingAssignments(Long studyId) {
        Member currentMember = memberUtil.getCurrentMember();
        LocalDate now = LocalDate.now();
        List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyId(studyId).stream()
                .filter(studyDetail ->
                        studyDetail.getAssignment().isOpen() && studyDetail.isAssignmentDeadlineThisWeek(now))
                .toList();
        List<AssignmentHistory> assignmentHistories =
                assignmentHistoryRepository.findAssignmentHistoriesByStudentAndStudyId(currentMember, studyId).stream()
                        .filter(assignmentHistory ->
                                assignmentHistory.getStudyDetail().isAssignmentDeadlineThisWeek(now))
                        .toList();

        return studyDetails.stream()
                .map(studyDetail -> AssignmentHistoryStatusResponse.of(
                        studyDetail, getSubmittedAssignment(assignmentHistories, studyDetail)))
                .toList();
    }
}
