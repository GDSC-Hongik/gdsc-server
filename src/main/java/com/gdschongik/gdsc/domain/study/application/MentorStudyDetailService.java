package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionStatus.SUCCESS;
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
        long studyCompletedStudentCount =
                studyHistories.stream().filter(StudyHistory::isComplete).count();

        List<StudyWeekStatisticsResponse> studyWeekStatisticsResponses = studyDetails.stream()
                .map((studyDetail -> calculateWeekStatistics(studyDetail, totalStudentCount)))
                .toList();

        long averageAttendanceRate = calculateAverageWeekAttendanceRate(studyWeekStatisticsResponses);
        long averageAssignmentSubmissionRate =
                calculateAverageWeekAssignmentSubmissionRate(studyWeekStatisticsResponses);

        return StudyStatisticsResponse.of(
                totalStudentCount,
                studyCompletedStudentCount,
                averageAttendanceRate,
                averageAssignmentSubmissionRate,
                studyWeekStatisticsResponses);
    }

    private StudyWeekStatisticsResponse calculateWeekStatistics(StudyDetail studyDetail, Long totalStudentCount) {
        boolean isNotOpenedCurriculum = !studyDetail.getCurriculum().isOpen();
        boolean isNotOpenedAssignment = !studyDetail.getAssignment().isOpen() || isNotOpenedCurriculum;

        if (totalStudentCount == 0) {
            return StudyWeekStatisticsResponse.empty(
                    studyDetail.getWeek(), isNotOpenedAssignment, isNotOpenedCurriculum);
        }

        if (isNotOpenedCurriculum) {
            return StudyWeekStatisticsResponse.canceledWeek(studyDetail.getWeek());
        }

        long attendanceCount = attendanceRepository.countByStudyDetailId(studyDetail.getId());
        long attendanceRate = Math.round(attendanceCount / (double) totalStudentCount * 100);

        if (isNotOpenedAssignment) {
            return StudyWeekStatisticsResponse.assignmentCanceled(studyDetail.getWeek(), attendanceRate);
        }

        long successfullySubmittedAssignmentCount =
                assignmentHistoryRepository.countByStudyDetailIdAndSubmissionStatusEquals(studyDetail.getId(), SUCCESS);
        long assignmentSubmissionRate =
                Math.round(successfullySubmittedAssignmentCount / (double) totalStudentCount * 100);

        return StudyWeekStatisticsResponse.opened(studyDetail.getWeek(), attendanceRate, assignmentSubmissionRate);
    }

    private long calculateAverageWeekAttendanceRate(List<StudyWeekStatisticsResponse> studyWeekStatisticsResponses) {

        double averageAttendanceRate = studyWeekStatisticsResponses.stream()
                .filter(weekStatisticsResponse -> !weekStatisticsResponse.isCanceledWeek())
                .mapToLong(StudyWeekStatisticsResponse::attendanceRate)
                .average()
                .orElse(0);

        return Math.round(averageAttendanceRate);
    }

    private long calculateAverageWeekAssignmentSubmissionRate(
            List<StudyWeekStatisticsResponse> studyWeekStatisticsResponses) {

        double averageAssignmentSubmissionRate = studyWeekStatisticsResponses.stream()
                .filter(studyWeekStatistics -> !studyWeekStatistics.isAssignmentCanceled())
                .mapToLong(StudyWeekStatisticsResponse::assignmentSubmissionRate)
                .average()
                .orElse(0);

        return Math.round(averageAssignmentSubmissionRate);
    }
}
