package com.gdschongik.gdsc.domain.studyv2.dto.response;

import static com.gdschongik.gdsc.domain.study.domain.AchievementType.*;
import static com.gdschongik.gdsc.domain.studyv2.dto.response.StudyTaskResponse.StudyTaskType.*;

import com.gdschongik.gdsc.domain.study.domain.AchievementType;
import com.gdschongik.gdsc.domain.study.domain.StudyHistoryStatus;
import com.gdschongik.gdsc.domain.studyv2.domain.AssignmentHistoryStatus;
import com.gdschongik.gdsc.domain.studyv2.domain.AttendanceStatus;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyAchievementV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record MentorStudyStudentResponse(
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

    public static MentorStudyStudentResponse of(
            StudyHistoryV2 studyHistory,
            List<StudyAchievementV2> studyAchievements,
            List<StudyTaskResponse> studyTasks) {
        List<StudyTaskResponse> assignments = studyTasks.stream()
                .filter(studyTaskResponse -> studyTaskResponse.taskType() == ASSIGNMENT)
                .toList();

        List<StudyTaskResponse> attendances = studyTasks.stream()
                .filter(studyTaskResponse -> studyTaskResponse.taskType() == ATTENDANCE)
                .toList();

        long successAssignmentsCount = countAssignmentByStatus(assignments, AssignmentHistoryStatus.SUCCEEDED);
        long attendedCount = countAttendanceByStatus(attendances, AttendanceStatus.ATTENDED);

        return new MentorStudyStudentResponse(
                studyHistory.getStudent().getId(),
                studyHistory.getStudent().getName(),
                studyHistory.getStudent().getStudentId(),
                studyHistory.getStudent().getDiscordUsername(),
                studyHistory.getStudent().getNickname(),
                studyHistory.getRepositoryLink(),
                studyHistory.getStatus(),
                isOutstandingStudent(FIRST_ROUND_OUTSTANDING_STUDENT, studyAchievements),
                isOutstandingStudent(SECOND_ROUND_OUTSTANDING_STUDENT, studyAchievements),
                studyTasks,
                calculateRateOrZero(successAssignmentsCount, assignments.size()),
                calculateRateOrZero(attendedCount, attendances.size()));
    }

    private static boolean isOutstandingStudent(
            AchievementType achievementType, List<StudyAchievementV2> studyAchievements) {
        return studyAchievements.stream().anyMatch(studyAchievement -> studyAchievement.getType() == achievementType);
    }

    private static long countAssignmentByStatus(List<StudyTaskResponse> assignments, AssignmentHistoryStatus status) {
        return assignments.stream()
                .filter(studyTaskResponse -> studyTaskResponse.assignmentSubmissionStatus() == status)
                .count();
    }

    private static long countAttendanceByStatus(List<StudyTaskResponse> attendances, AttendanceStatus status) {
        return attendances.stream()
                .filter(studyTaskResponse -> studyTaskResponse.attendanceStatus() == status)
                .count();
    }

    private static double calculateRateOrZero(long dividend, long divisor) {
        return divisor == 0 ? 0 : (double) dividend * 100 / divisor;
    }
}
