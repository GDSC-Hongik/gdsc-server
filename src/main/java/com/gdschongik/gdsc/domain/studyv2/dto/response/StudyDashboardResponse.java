package com.gdschongik.gdsc.domain.studyv2.dto.response;

import com.gdschongik.gdsc.domain.studyv2.domain.AssignmentHistoryStatus;
import com.gdschongik.gdsc.domain.studyv2.domain.AssignmentHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.AttendanceV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudySessionV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudySessionMyDto;
import java.time.LocalDateTime;
import java.util.List;

public record StudyDashboardResponse(List<StudySessionMyDto> sessions) {
    public static StudyDashboardResponse of(
            StudyV2 study,
            List<AttendanceV2> attendances,
            List<AssignmentHistoryV2> assignmentHistories,
            LocalDateTime now) {
        List<StudySessionMyDto> studySessions = study.getStudySessions().stream()
                .map(studySession -> StudySessionMyDto.of(
                        studySession,
                        getAssignmentHistory(studySession, assignmentHistories),
                        study.getType(),
                        isAttended(studySession, attendances),
                        now))
                .toList();

        return new StudyDashboardResponse(studySessions);
    }

    private static boolean isAttended(StudySessionV2 studySession, List<AttendanceV2> attendances) {
        return attendances.stream()
                .anyMatch(attendance -> attendance.getStudySession().getId().equals(studySession.getId()));
    }

    private static AssignmentHistoryV2 getAssignmentHistory(
            StudySessionV2 studySession, List<AssignmentHistoryV2> assignmentHistories) {
        return assignmentHistories.stream()
                .filter(assignmentHistory -> isEquals(studySession, assignmentHistory))
                .filter(assignmentHistory ->
                        isCommittedAtWithinAssignmentPeriodIfExist(assignmentHistory, studySession))
                .findFirst()
                .orElse(null);
    }

    private static boolean isEquals(StudySessionV2 studySession, AssignmentHistoryV2 assignmentHistory) {
        return assignmentHistory.getStudySession().getId().equals(studySession.getId());
    }

    /**
     * 과제 제출 이력이 있는 경우, 제출 시간이 과제 제출 기간 내에 있는지 확인합니다.
     * 과제 제출 이력이 없는 경우, 항상 true를 반환합니다.
     *
     * @see AssignmentHistoryStatus
     */
    private static boolean isCommittedAtWithinAssignmentPeriodIfExist(
            AssignmentHistoryV2 assignmentHistory, StudySessionV2 studySession) {
        if (assignmentHistory == null) {
            return true;
        }

        LocalDateTime committedAt = assignmentHistory.getCommittedAt();
        return studySession.getAssignmentPeriod().isWithin(committedAt);
    }
}
