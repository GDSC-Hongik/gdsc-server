package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.StudyDetailValidator;
import com.gdschongik.gdsc.domain.study.dto.request.AssignmentCreateUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.response.AssignmentResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyMentorAttendanceResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudySessionResponse;
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
    public void publishStudyAssignment(Long studyDetailId, AssignmentCreateUpdateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyDetail studyDetail = studyDetailRepository
                .findById(studyDetailId)
                .orElseThrow(() -> new CustomException(STUDY_DETAIL_NOT_FOUND));

        studyDetailValidator.validatePublishStudyAssignment(currentMember, studyDetail, request);

        studyDetail.publishAssignment(request.title(), request.deadLine(), request.descriptionNotionLink());
        studyDetailRepository.save(studyDetail);

        log.info("[MentorStudyDetailService] 과제 개설 완료: studyDetailId={}", studyDetailId);
    }

    @Transactional
    public void updateStudyAssignment(Long studyDetailId, AssignmentCreateUpdateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyDetail studyDetail = studyDetailRepository
                .findById(studyDetailId)
                .orElseThrow(() -> new CustomException(STUDY_DETAIL_NOT_FOUND));

        studyDetailValidator.validateUpdateStudyAssignment(currentMember, studyDetail, request);

        studyDetail.updateAssignment(request.title(), request.deadLine(), request.descriptionNotionLink());
        studyDetailRepository.save(studyDetail);

        log.info("[MentorStudyDetailService] 과제 수정 완료: studyDetailId={}", studyDetailId);
    }

    @Transactional(readOnly = true)
    public List<StudySessionResponse> getSessions(Long studyId) {
        List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyIdOrderByWeekAsc(studyId);
        return studyDetails.stream().map(StudySessionResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<StudyMentorAttendanceResponse> getAttendanceNumber(Long studyId) {
        List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyIdOrderByWeekAsc(studyId);

        // 출석일이 오늘 or 오늘이후 인 StudyDetail
        List<StudyDetail> notAttendedStudyDetails = studyDetails.stream()
                .filter(studyDetail -> !studyDetail.getAttendanceDay().isBefore(LocalDate.now()))
                .toList();
        return notAttendedStudyDetails.stream()
                .map(StudyMentorAttendanceResponse::from)
                .toList();
    }
}
