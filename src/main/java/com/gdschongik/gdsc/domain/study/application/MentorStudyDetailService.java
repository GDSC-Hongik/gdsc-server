package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionStatus.SUCCESS;
import static com.gdschongik.gdsc.domain.study.dto.response.StudyWeekStatisticsResponse.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.AssignmentHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.AttendanceRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.StudyDetailValidator;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.gdschongik.gdsc.domain.study.domain.StudyValidator;
import com.gdschongik.gdsc.domain.study.dto.request.AssignmentCreateUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.response.AssignmentResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyCurriculumResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyMentorAttendanceResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyStatisticsResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyWeekStatisticsResponse;
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
        List<StudyHistory> studyHistories = studyHistoryRepository.findAllByStudyId(studyId);
        List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyIdOrderByWeekAsc(studyId);
        studyValidator.validateStudyMentor(currentMember, study);

        long totalStudentCount = studyHistories.size();
        long studyCompleteStudentCount =
                studyHistories.stream().filter(StudyHistory::isComplete).count();

        List<StudyWeekStatisticsResponse> studyWeekStatisticsResponses =
                calculateStudyWeekStatistics(studyDetails, totalStudentCount);

        long averageAttendanceRate = calculateAverageWeekAttendanceRate(studyWeekStatisticsResponses);
        long averageAssignmentSubmitRate = calculateAverageWeekAssignmentSubmitRate(studyWeekStatisticsResponses);

        return StudyStatisticsResponse.of(
                totalStudentCount,
                studyCompleteStudentCount,
                averageAttendanceRate,
                averageAssignmentSubmitRate,
                studyWeekStatisticsResponses);
    }

    private List<StudyWeekStatisticsResponse> calculateStudyWeekStatistics(
            List<StudyDetail> studyDetails, Long totalStudentCount) {

        return studyDetails.stream()
                .map((studyDetail -> {
                    boolean isCanceledWeek = !studyDetail.getCurriculum().isOpen();
                    boolean isCanceledAssignment = !studyDetail.getAssignment().isOpen() | isCanceledWeek;

                    if (totalStudentCount == 0) {
                        return of(studyDetail.getWeek(), 0L, 0L, isCanceledAssignment, isCanceledWeek);
                    }

                    long attendanceCount = attendanceRepository.countByStudyDetailId(studyDetail.getId());
                    long assignmentCount = assignmentHistoryRepository.countByStudyDetailIdAndSubmissionStatusEquals(
                            studyDetail.getId(), SUCCESS);

                    return of(
                            studyDetail.getWeek(),
                            isCanceledWeek ? 0 : Math.round(attendanceCount / (double) totalStudentCount * 100),
                            isCanceledAssignment ? 0 : Math.round(assignmentCount / (double) totalStudentCount * 100),
                            isCanceledAssignment,
                            isCanceledWeek);
                }))
                .toList();
    }

    private long calculateAverageWeekAttendanceRate(List<StudyWeekStatisticsResponse> studyWeekStatisticsResponses) {

        double averageAttendanceRate = studyWeekStatisticsResponses.stream()
                .filter(weekStatisticsResponse -> !weekStatisticsResponse.isCanceledWeek())
                .mapToLong(StudyWeekStatisticsResponse::attendanceRate)
                .average()
                .orElse(0);

        return Math.round(averageAttendanceRate);
    }

    private long calculateAverageWeekAssignmentSubmitRate(
            List<StudyWeekStatisticsResponse> studyWeekStatisticsResponses) {

        double averageAssignmentSubmitRate = studyWeekStatisticsResponses.stream()
                .filter(studyWeekStatistics -> !studyWeekStatistics.isCanceledAssignment())
                .mapToLong(StudyWeekStatisticsResponse::assignmentSubmitRate)
                .average()
                .orElse(0);

        return Math.round(averageAssignmentSubmitRate);
    }
}
