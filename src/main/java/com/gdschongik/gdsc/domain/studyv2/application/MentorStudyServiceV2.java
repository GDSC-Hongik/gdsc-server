package com.gdschongik.gdsc.domain.studyv2.application;

import static com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionStatus.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static java.util.stream.Collectors.groupingBy;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.StudyType;
import com.gdschongik.gdsc.domain.studyv2.dao.*;
import com.gdschongik.gdsc.domain.studyv2.domain.*;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudyRoundStatisticsDto;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudyTaskDto;
import com.gdschongik.gdsc.domain.studyv2.dto.request.StudyUpdateRequest;
import com.gdschongik.gdsc.domain.studyv2.dto.response.*;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorStudyServiceV2 {

    private final MemberUtil memberUtil;
    private final StudyValidatorV2 studyValidatorV2;
    private final StudyV2Repository studyV2Repository;
    private final StudyHistoryV2Repository studyHistoryV2Repository;
    private final StudyAchievementV2Repository studyAchievementV2Repository;
    private final AttendanceV2Repository attendanceV2Repository;
    private final AssignmentHistoryV2Repository assignmentHistoryV2Repository;

    @Transactional(readOnly = true)
    public List<StudyManagerResponse> getStudiesInCharge() {
        Member mentor = memberUtil.getCurrentMember();
        List<StudyV2> myStudies = studyV2Repository.findAllByMentor(mentor);
        return myStudies.stream().map(StudyManagerResponse::from).toList();
    }

    @Transactional
    public void updateStudy(Long studyId, StudyUpdateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyV2 study =
                studyV2Repository.findFetchById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));

        studyValidatorV2.validateStudyMentor(currentMember, study);

        StudyUpdateCommand command = request.toCommand();

        study.update(command);
        studyV2Repository.save(study);

        log.info("[MentorStudyServiceV2] 스터디 정보 수정 완료: studyId={}", studyId);
    }

    @Transactional(readOnly = true)
    public StudyStatisticsResponse getStudyStatistics(Long studyId) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyV2 study = studyV2Repository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));

        studyValidatorV2.validateStudyMentor(currentMember, study);

        List<StudyHistoryV2> studyHistories = studyHistoryV2Repository.findAllByStudy(study);
        List<StudySessionV2> studySessions = study.getStudySessions();

        long totalStudentCount = studyHistories.size();
        long studyCompletedStudentCount =
                studyHistories.stream().filter(StudyHistoryV2::isCompleted).count();

        List<StudyRoundStatisticsDto> studyRoundStatisticsDtos = studySessions.stream()
                .map(studySessionV2 -> calculateRoundStatistics(studySessionV2, totalStudentCount))
                .toList();

        long averageAttendanceRate = calculateAverageWeekAttendanceRate(studyRoundStatisticsDtos);
        long averageAssignmentSubmissionRate = calculateAverageWeekAssignmentSubmissionRate(studyRoundStatisticsDtos);

        return StudyStatisticsResponse.of(
                totalStudentCount,
                studyCompletedStudentCount,
                averageAttendanceRate,
                averageAssignmentSubmissionRate,
                studyRoundStatisticsDtos);
    }

    @Transactional
    public Page<MentorStudyStudentResponse> getStudyStudents(Long studyId, Pageable pageable) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyV2 study = studyV2Repository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        studyValidatorV2.validateStudyMentor(currentMember, study);

        LocalDateTime now = LocalDateTime.now();
        StudyType type = study.getType();
        Page<StudyHistoryV2> studyHistories = studyHistoryV2Repository.findByStudyId(studyId, pageable);
        List<Long> studentIds = studyHistories.stream()
                .map(studyHistory -> studyHistory.getStudent().getId())
                .toList();
        List<StudySessionV2> studySessions = study.getStudySessions();

        Map<Long, List<StudyAchievementV2>> studyAchievementMap = getStudyAchievementMap(studyId, studentIds);
        Map<Long, List<AttendanceV2>> attendanceMap = getAttendanceMap(studyId, studentIds);
        Map<Long, List<AssignmentHistoryV2>> assignmentHistoryMap = getAssignmentHistoryMap(studyId, studentIds);

        List<MentorStudyStudentResponse> response = new ArrayList<>();

        studyHistories.forEach(studyHistory -> {
            List<StudyAchievementV2> currentStudyAchievements =
                    studyAchievementMap.getOrDefault(studyHistory.getStudent().getId(), List.of());
            List<AttendanceV2> currentAttendances =
                    attendanceMap.getOrDefault(studyHistory.getStudent().getId(), List.of());
            List<AssignmentHistoryV2> currentAssignmentHistories =
                    assignmentHistoryMap.getOrDefault(studyHistory.getStudent().getId(), List.of());

            List<StudyTaskDto> studyTasks = new ArrayList<>();
            studySessions.forEach(studySession -> {
                studyTasks.add(StudyTaskDto.of(studySession, type, isAttended(currentAttendances, studySession), now));
                studyTasks.add(StudyTaskDto.of(
                        studySession, getSubmittedAssignment(currentAssignmentHistories, studySession), now));
            });

            response.add(MentorStudyStudentResponse.of(studyHistory, currentStudyAchievements, studyTasks));
        });
        return new PageImpl<>(response, pageable, studyHistories.getTotalElements());
    }

    private StudyRoundStatisticsDto calculateRoundStatistics(StudySessionV2 studySessionV2, Long totalStudentCount) {
        long attendanceCount = attendanceV2Repository.countByStudySessionId(studySessionV2.getId());
        long attendanceRate = Math.round(attendanceCount / (double) totalStudentCount * 100);

        long successfullySubmittedAssignmentCount =
                assignmentHistoryV2Repository.countByStudySessionIdAndSubmissionStatusEquals(
                        studySessionV2.getId(), SUCCESS);
        long assignmentSubmissionRate =
                Math.round(successfullySubmittedAssignmentCount / (double) totalStudentCount * 100);

        return StudyRoundStatisticsDto.of(studySessionV2.getPosition(), attendanceRate, assignmentSubmissionRate);
    }

    private long calculateAverageWeekAttendanceRate(List<StudyRoundStatisticsDto> studyRoundStatisticsDtos) {

        double averageAttendanceRate = studyRoundStatisticsDtos.stream()
                .mapToLong(StudyRoundStatisticsDto::attendanceRate)
                .average()
                .orElse(0);

        return Math.round(averageAttendanceRate);
    }

    private long calculateAverageWeekAssignmentSubmissionRate(List<StudyRoundStatisticsDto> studyRoundStatisticsDtos) {

        double averageAssignmentSubmissionRate = studyRoundStatisticsDtos.stream()
                .mapToLong(StudyRoundStatisticsDto::assignmentSubmissionRate)
                .average()
                .orElse(0);

        return Math.round(averageAssignmentSubmissionRate);
    }

    private Map<Long, List<StudyAchievementV2>> getStudyAchievementMap(Long studyId, List<Long> studentIds) {
        List<StudyAchievementV2> studyAchievements =
                studyAchievementV2Repository.findByStudyIdAndMemberIds(studyId, studentIds);
        return studyAchievements.stream()
                .collect(groupingBy(
                        studyAchievement -> studyAchievement.getStudent().getId()));
    }

    private Map<Long, List<AttendanceV2>> getAttendanceMap(Long studyId, List<Long> studentIds) {
        List<AttendanceV2> attendances = attendanceV2Repository.findByStudyIdAndMemberIds(studyId, studentIds);
        return attendances.stream()
                .collect(groupingBy(attendance -> attendance.getStudent().getId()));
    }

    private Map<Long, List<AssignmentHistoryV2>> getAssignmentHistoryMap(Long studyId, List<Long> studentIds) {
        List<AssignmentHistoryV2> assignmentHistories =
                assignmentHistoryV2Repository.findByStudyIdAndMemberIds(studyId, studentIds);
        return assignmentHistories.stream()
                .collect(groupingBy(
                        assignmentHistory -> assignmentHistory.getMember().getId()));
    }

    private boolean isAttended(List<AttendanceV2> attendances, StudySessionV2 studySessionV2) {
        return attendances.stream()
                .anyMatch(attendance -> attendance.getStudySession().getId().equals(studySessionV2.getId()));
    }

    private AssignmentHistoryV2 getSubmittedAssignment(
            List<AssignmentHistoryV2> assignmentHistories, StudySessionV2 studySessionV2) {
        return assignmentHistories.stream()
                .filter(assignmentHistory ->
                        assignmentHistory.getStudySession().getId().equals(studySessionV2.getId()))
                .findFirst()
                .orElse(null);
    }
}
