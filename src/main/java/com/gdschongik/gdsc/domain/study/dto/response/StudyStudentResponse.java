package com.gdschongik.gdsc.domain.study.dto.response;

import static com.gdschongik.gdsc.domain.study.domain.AchievementType.*;
import static com.gdschongik.gdsc.domain.study.dto.response.AssignmentSubmissionStatusResponse.*;
import static com.gdschongik.gdsc.domain.study.dto.response.StudyTodoResponse.StudyTodoType.*;

import com.gdschongik.gdsc.domain.study.domain.AchievementType;
import com.gdschongik.gdsc.domain.study.domain.StudyAchievement;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record StudyStudentResponse(
        @Schema(description = "멤버 아이디") Long memberId,
        @Schema(description = "학생 이름") String name,
        @Schema(description = "학번") String studentId,
        @Schema(description = "디스코드 사용자명") String discordUsername,
        @Schema(description = "디스코드 닉네임") String nickname,
        @Schema(description = "깃허브 링크") String githubLink,
        @Schema(description = "1차 우수 스터디원") boolean isFirstRoundOutstandingStudent,
        @Schema(description = "2차 우수 스터디원") boolean isSecondRoundOutstandingStudent,
        @Schema(description = "과제 및 출석 이력") List<StudyTodoResponse> studyTodos,
        @Schema(description = "과제 수행률") double assignmentRate,
        @Schema(description = "출석률") double attendanceRate) {
    public static StudyStudentResponse of(
            StudyHistory studyHistory, List<StudyAchievement> studyAchievements, List<StudyTodoResponse> studyTodos) {
        List<StudyTodoResponse> assignments = studyTodos.stream()
                .filter(studyTodoResponse -> studyTodoResponse.todoType() == ASSIGNMENT)
                .toList();

        List<StudyTodoResponse> attendances = studyTodos.stream()
                .filter(studyTodoResponse -> studyTodoResponse.todoType() == ATTENDANCE)
                .toList();

        long successAssignmentsCount = countAssignmentByStatus(assignments, SUCCESS);
        long cancelledAssignmentsCount = countAssignmentByStatus(assignments, CANCELLED);

        long attendedCount = countAttendanceByStatus(attendances, AttendanceStatusResponse.ATTENDED);
        long cancelledAttendanceCount = countAttendanceByStatus(attendances, AttendanceStatusResponse.CANCELLED);

        return new StudyStudentResponse(
                studyHistory.getStudent().getId(),
                studyHistory.getStudent().getName(),
                studyHistory.getStudent().getStudentId(),
                studyHistory.getStudent().getDiscordUsername(),
                studyHistory.getStudent().getNickname(),
                studyHistory.getRepositoryLink(),
                isOutstandingStudent(FIRST_ROUND_OUTSTANDING_STUDENT, studyAchievements),
                isOutstandingStudent(SECOND_ROUND_OUTSTANDING_STUDENT, studyAchievements),
                studyTodos,
                assignments.size() != cancelledAssignmentsCount
                        ? (double) successAssignmentsCount * 100 / (assignments.size() - cancelledAssignmentsCount)
                        : 0,
                attendances.size() != cancelledAttendanceCount
                        ? (double) (attendedCount * 100) / (attendances.size() - cancelledAttendanceCount)
                        : 0);
    }

    private static boolean isOutstandingStudent(
            AchievementType achievementType, List<StudyAchievement> studyAchievements) {
        return studyAchievements.stream()
                .anyMatch(studyAchievement -> studyAchievement.getAchievementType() == achievementType);
    }

    private static long countAssignmentByStatus(
            List<StudyTodoResponse> assignments, AssignmentSubmissionStatusResponse status) {
        return assignments.stream()
                .filter(studyTodoResponse -> studyTodoResponse.assignmentSubmissionStatus() == status)
                .count();
    }

    private static long countAttendanceByStatus(List<StudyTodoResponse> attendances, AttendanceStatusResponse status) {
        return attendances.stream()
                .filter(studyTodoResponse -> studyTodoResponse.attendanceStatus() == status)
                .count();
    }
}
