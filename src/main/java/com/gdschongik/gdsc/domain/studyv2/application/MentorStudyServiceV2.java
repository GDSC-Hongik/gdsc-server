package com.gdschongik.gdsc.domain.studyv2.application;

import static com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionStatus.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.dao.AssignmentHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.AttendanceV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudySessionV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyUpdateCommand;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyValidatorV2;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudyRoundStatisticsDto;
import com.gdschongik.gdsc.domain.studyv2.dto.request.StudyUpdateRequest;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyManagerResponse;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyStatisticsResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
