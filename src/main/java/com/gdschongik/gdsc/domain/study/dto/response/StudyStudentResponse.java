package com.gdschongik.gdsc.domain.study.dto.response;

import static com.gdschongik.gdsc.domain.study.domain.AchievementType.*;
import static com.gdschongik.gdsc.domain.study.dto.response.AssignmentSubmissionStatusResponse.*;
import static com.gdschongik.gdsc.domain.study.dto.response.StudyTaskResponse.StudyTaskType.*;

import com.gdschongik.gdsc.domain.study.domain.AchievementType;
import com.gdschongik.gdsc.domain.study.domain.StudyAchievement;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.gdschongik.gdsc.domain.study.domain.StudyHistoryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record StudyStudentResponse(
        @Schema(description = "멤버 아이디") Long memberId,
        @Schema(description = "학생 이름") String name,
        @Schema(description = "학번") String studentId,
        @Schema(description = "디스코드 사용자명") String discordUsername,
        @Schema(description = "디스코드 닉네임") String nickname,
        @Schema(description = "깃허브 링크") String githubLink,
        @Schema(description = "수료 상태") StudyHistoryStatus studyHistoryStatus,
        @Schema(description = "1차 우수 스터디원") boolean isFirstRoundOutstandingStudent,
        @Schema(description = "2차 우수 스터디원") boolean isSecondRoundOutstandingStudent,
        @Schema(description = "과제 및 출석 이력") List<StudyTaskResponse> studyTasks,
        @Schema(description = "과제 수행률") double assignmentRate,
        @Schema(description = "출석률") double attendanceRate) {
    public static StudyStudentResponse of(
            StudyHistory studyHistory, List<StudyAchievement> studyAchievements, List<StudyTaskResponse> studyTasks) {
        List<StudyTaskResponse> assignments = studyTasks.stream()
                .filter(studyTaskResponse -> studyTaskResponse.taskType() == ASSIGNMENT)
                .toList();

        List<StudyTaskResponse> attendances = studyTasks.stream()
                .filter(studyTaskResponse -> studyTaskResponse.taskType() == ATTENDANCE)
                .toList();

        long successAssignmentsCount = countAssignmentByStatus(assignments, SUCCESS);
        long canceledAssignmentsCount = countAssignmentByStatus(assignments, CANCELED);

        long attendedCount = countAttendanceByStatus(attendances, AttendanceStatusResponse.ATTENDED);
        long canceledAttendanceCount = countAttendanceByStatus(attendances, AttendanceStatusResponse.CANCELED);

        return new StudyStudentResponse(
                studyHistory.getStudent().getId(),
                studyHistory.getStudent().getName(),
                studyHistory.getStudent().getStudentId(),
                studyHistory.getStudent().getDiscordUsername(),
                studyHistory.getStudent().getNickname(),
                studyHistory.getRepositoryLink(),
                studyHistory.getStudyHistoryStatus(),
                isOutstandingStudent(FIRST_ROUND_OUTSTANDING_STUDENT, studyAchievements),
                isOutstandingStudent(SECOND_ROUND_OUTSTANDING_STUDENT, studyAchievements),
                studyTasks,
                calculateRateOrZero(successAssignmentsCount, assignments.size() - canceledAssignmentsCount),
                calculateRateOrZero(attendedCount, attendances.size() - canceledAttendanceCount));
    }

    private static boolean isOutstandingStudent(
            AchievementType achievementType, List<StudyAchievement> studyAchievements) {
        return studyAchievements.stream()
                .anyMatch(studyAchievement -> studyAchievement.getAchievementType() == achievementType);
    }

    private static long countAssignmentByStatus(
            List<StudyTaskResponse> assignments, AssignmentSubmissionStatusResponse status) {
        return assignments.stream()
                .filter(studyTaskResponse -> studyTaskResponse.assignmentSubmissionStatus() == status)
                .count();
    }

    private static long countAttendanceByStatus(List<StudyTaskResponse> attendances, AttendanceStatusResponse status) {
        return attendances.stream()
                .filter(studyTaskResponse -> studyTaskResponse.attendanceStatus() == status)
                .count();
    }

    private static double calculateRateOrZero(long dividend, long divisor) {
        return divisor == 0 ? 0 : (double) dividend * 100 / divisor;
    }
}
