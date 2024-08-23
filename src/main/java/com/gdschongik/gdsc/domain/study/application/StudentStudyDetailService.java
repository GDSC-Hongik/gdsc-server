package com.gdschongik.gdsc.domain.study.application;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.AssignmentHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.AttendanceRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import com.gdschongik.gdsc.domain.study.domain.Attendance;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.gdschongik.gdsc.domain.study.dto.response.AssignmentDashboardResponse;
import com.gdschongik.gdsc.domain.study.dto.response.AssignmentHistoryStatusResponse;
import com.gdschongik.gdsc.domain.study.dto.response.AssignmentSubmittableDto;
import com.gdschongik.gdsc.domain.study.dto.response.StudyStudentSessionResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentStudyDetailService {

    private final MemberUtil memberUtil;
    private final StudyRepository studyRepository;
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
        boolean isAnySubmitted = assignmentHistories.stream().anyMatch(AssignmentHistory::isSubmitted);
        List<AssignmentSubmittableDto> submittableAssignments = assignmentHistories.stream()
                .filter(assignmentHistory -> assignmentHistory.getStudyDetail().isAssignmentDeadlineRemaining())
                .map(AssignmentSubmittableDto::from)
                .toList();

        return AssignmentDashboardResponse.of(studyHistory.getRepositoryLink(), isAnySubmitted, submittableAssignments);
    }

    @Transactional(readOnly = true)
    public List<AssignmentHistoryStatusResponse> getUpcomingAssignments(Long studyId) {
        Member currentMember = memberUtil.getCurrentMember();
        List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyId(studyId).stream()
                .filter(studyDetail -> studyDetail.isAssignmentDeadlineThisWeek())
                .toList();
        List<AssignmentHistory> assignmentHistories =
                assignmentHistoryRepository.findAssignmentHistoriesByStudentAndStudyId(currentMember, studyId).stream()
                        .filter(assignmentHistory ->
                                assignmentHistory.getStudyDetail().isAssignmentDeadlineThisWeek())
                        .toList();
        List<AssignmentHistoryStatusResponse> response = studyDetails.stream().map(studyDetail -> AssignmentHistoryStatusResponse.of(studyDetail,assignmentHistories)).toList();

        if (assignmentHistories.isEmpty()) {
            return studyDetails.stream()
                    .map(AssignmentHistoryStatusResponse::fromStudyDetail)
                    .toList();
        }
        return assignmentHistories.stream()
                .map(AssignmentHistoryStatusResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StudyStudentSessionResponse> getStudySessions(Long studyId) {
        Member member = memberUtil.getCurrentMember();
        final List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyIdOrderByWeekAsc(studyId);
        final List<AssignmentHistory> assignmentHistories =
                assignmentHistoryRepository.findAssignmentHistoriesByStudentAndStudyId(member, studyId);
        final List<Attendance> attendances = attendanceRepository.findByMemberAndStudyId(member, studyId);

        return studyDetails.stream()
                .map(studyDetail -> StudyStudentSessionResponse.of(
                        studyDetail,
                        getSubmittedAssignment(assignmentHistories, studyDetail),
                        isAttended(attendances, studyDetail),
                        LocalDateTime.now()))
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
}
