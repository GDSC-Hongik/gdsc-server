package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.*;
import com.gdschongik.gdsc.domain.study.domain.*;
import com.gdschongik.gdsc.domain.study.dto.request.AssignmentCreateUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.response.*;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorStudyDetailService {

    private final MemberUtil memberUtil;
    private final StudyDetailRepository studyDetailRepository;
    private final StudyDetailValidator studyDetailValidator;
    private final StudyHistoryRepository studyHistoryRepository;
    private final AttendanceRepository attendanceRepository;
    private final AssignmentHistoryRepository assignmentHistoryRepository;
    private final StudyValidator studyValidator;
    private final StudyRepository studyRepository;

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getWeeklyAssignments(Long studyId) {
        List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyIdOrderByWeekAsc(studyId);
        return studyDetails.stream().map(AssignmentResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public AssignmentResponse getAssignment(Long studyDetailId) {
        StudyDetail studyDetail = studyDetailRepository
                .findById(studyDetailId)
                .orElseThrow(() -> new CustomException(STUDY_DETAIL_NOT_FOUND));
        return AssignmentResponse.from(studyDetail);
    }

    @Transactional
    public void cancelStudyAssignment(Long studyDetailId) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyDetail studyDetail = studyDetailRepository
                .findById(studyDetailId)
                .orElseThrow(() -> new CustomException(STUDY_DETAIL_NOT_FOUND));

        studyDetailValidator.validateCancelStudyAssignment(currentMember, studyDetail);

        studyDetail.cancelAssignment();
        studyDetailRepository.save(studyDetail);

        log.info("[MentorStudyDetailService] 과제 휴강 처리: studyDetailId={}", studyDetail.getId());
    }

    @Transactional
    public AssignmentResponse publishStudyAssignment(Long studyDetailId, AssignmentCreateUpdateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyDetail studyDetail = studyDetailRepository
                .findById(studyDetailId)
                .orElseThrow(() -> new CustomException(STUDY_DETAIL_NOT_FOUND));

        studyDetailValidator.validatePublishStudyAssignment(currentMember, studyDetail, request);

        studyDetail.publishAssignment(request.title(), request.deadLine(), request.descriptionNotionLink());
        StudyDetail savedStudyDetail = studyDetailRepository.save(studyDetail);

        log.info("[MentorStudyDetailService] 과제 개설 완료: studyDetailId={}", studyDetailId);

        return AssignmentResponse.from(savedStudyDetail);
    }

    @Transactional
    public AssignmentResponse updateStudyAssignment(Long studyDetailId, AssignmentCreateUpdateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyDetail studyDetail = studyDetailRepository
                .findById(studyDetailId)
                .orElseThrow(() -> new CustomException(STUDY_DETAIL_NOT_FOUND));

        studyDetailValidator.validateUpdateStudyAssignment(currentMember, studyDetail, request);

        studyDetail.updateAssignment(request.title(), request.deadLine(), request.descriptionNotionLink());
        StudyDetail savedStudyDetail = studyDetailRepository.save(studyDetail);

        log.info("[MentorStudyDetailService] 과제 수정 완료: studyDetailId={}", studyDetailId);

        return AssignmentResponse.from(savedStudyDetail);
    }

    @Transactional(readOnly = true)
    public List<StudyCurriculumResponse> getCurriculums(Long studyId) {
        List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyIdOrderByWeekAsc(studyId);
        return studyDetails.stream().map(StudyCurriculumResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<StudyMentorAttendanceResponse> getAttendanceNumbers(Long studyId) {
        List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyIdOrderByWeekAsc(studyId);

        // 출석일이 오늘 or 오늘이후인 StudyDetail
        return studyDetails.stream()
                .filter(studyDetail -> studyDetail.isAttendanceDayNotPassed(LocalDate.now()))
                .map(StudyMentorAttendanceResponse::from)
                .limit(2)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudyStatisticsResponse getStudyStatistics(Long studyId) {
        Member currentMember = memberUtil.getCurrentMember();
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        studyValidator.validateStudyMentor(currentMember, study);

        List<StudyHistory> studyHistories = studyHistoryRepository.findAllByStudyId(studyId);
        long wholeStudentCount = studyHistories.size();
        long studyCompleteStudentCount =
                studyHistories.stream().filter(StudyHistory::isComplete).count();

        List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyIdOrderByWeekAsc(studyId);
        long openedWeekCount = studyDetails.stream()
                .filter(studyDetail -> studyDetail.getCurriculum().isOpen())
                .count();

        List<StudyWeekStatisticsResponse> studyWeekStatisticsResponses =
                calculateStudyWeekStatistics(studyDetails, wholeStudentCount);

        long averageAttendanceRate = calculateAverageWeekAttendanceRate(studyWeekStatisticsResponses, openedWeekCount);
        long averageAssignmentSubmitRate =
                calculateAverageWeekAssignmentSubmitRate(studyWeekStatisticsResponses, openedWeekCount);

        return StudyStatisticsResponse.of(
                wholeStudentCount,
                studyCompleteStudentCount,
                averageAttendanceRate,
                averageAssignmentSubmitRate,
                studyWeekStatisticsResponses);
    }

    private List<StudyWeekStatisticsResponse> calculateStudyWeekStatistics(
            List<StudyDetail> studyDetails, Long wholeStudentCount) {

        return studyDetails.stream()
                .map((studyDetail -> {
                    if (!studyDetail.getCurriculum().isOpen()) {
                        return StudyWeekStatisticsResponse.createCanceledWeekStatistics(studyDetail.getWeek());
                    }

                    if (wholeStudentCount == 0) {
                        return StudyWeekStatisticsResponse.createOpenedWeekStatistics(studyDetail.getWeek(), 0L, 0L);
                    }

                    long attendanceCount = attendanceRepository.countByStudyDetailId(studyDetail.getId());
                    long assignmentCount = assignmentHistoryRepository.countByStudyDetailId(studyDetail.getId());

                    return StudyWeekStatisticsResponse.createOpenedWeekStatistics(
                            studyDetail.getWeek(),
                            Math.round(attendanceCount / (double) wholeStudentCount * 100),
                            Math.round(assignmentCount / (double) wholeStudentCount * 100));
                }))
                .toList();
    }

    private long calculateAverageWeekAttendanceRate(
            List<StudyWeekStatisticsResponse> studyWeekStatisticsResponses, long openedWeekCount) {

        if (openedWeekCount == 0) {
            return 0;
        }

        long attendanceRateSum = studyWeekStatisticsResponses.stream()
                .mapToLong(
                        weekStatistics -> weekStatistics.isCanceledWeek() ? 0 : weekStatistics.attendanceRate())
                .sum();

        return Math.round(attendanceRateSum / (double) openedWeekCount * 100);
    }

    private long calculateAverageWeekAssignmentSubmitRate(
            List<StudyWeekStatisticsResponse> studyWeekStatisticsResponses, long openedWeekCount) {

        if (openedWeekCount == 0) {
            return 0;
        }

        long assignmentSubmitRateSum = studyWeekStatisticsResponses.stream()
                        .mapToLong(weekStatistics ->
                                weekStatistics.isCanceledWeek() ? 0 : weekStatistics.assignmentSubmitRate())
                        .sum();

        return Math.round(assignmentSubmitRateSum / (double) openedWeekCount * 100);
    }
}
