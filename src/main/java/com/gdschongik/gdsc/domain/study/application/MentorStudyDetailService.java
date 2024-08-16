package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.StudyDetailValidator;
import com.gdschongik.gdsc.domain.study.dto.request.AssignmentCreateUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.request.StudyDetailUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.request.StudySessionCreateRequest;
import com.gdschongik.gdsc.domain.study.dto.response.AssignmentResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudySessionResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
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
    private final StudyRepository studyRepository;

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getWeeklyAssignments(Long studyId) {
        List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyId(studyId);
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
        List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyId(studyId);
        return studyDetails.stream().map(StudySessionResponse::from).toList();
    }

    @Transactional
    public void updateStudyDetail(Long studyId, StudyDetailUpdateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));

        // TODO studyValidator에서 mentor인지 검증하는 validator사용하기

        List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyId(studyId);
        studyDetailValidator.validateUpdateStudyDetail(studyDetails, request.studySessions());

        // 스터디 저장
        study.update(request.notionLink(), request.introduction());
        studyRepository.save(study);

        Map<Long, StudySessionCreateRequest> requestMap = request.studySessions().stream()
                .collect(Collectors.toMap(StudySessionCreateRequest::studyDetailId, Function.identity()));

        // StudyDetail을 업데이트하는 작업
        List<StudyDetail> updatedStudyDetails = new ArrayList<>();
        for (StudyDetail studyDetail : studyDetails) {
            Long id = studyDetail.getId();
            StudySessionCreateRequest matchingSession = requestMap.get(id);

            studyDetail.updateSession(
                    studyDetail.getStudy().getPeriod().getStartDate(),
                    matchingSession.title(),
                    matchingSession.description(),
                    matchingSession.difficulty(),
                    matchingSession.status());

            updatedStudyDetails.add(studyDetail);
        }
        //
        //        // 세션 정보 bulk insert
        //        List<StudyDetail> updatedStudyDetails = new ArrayList<>();
        //        for (StudyDetail studyDetail : studyDetails) {
        //            StudySessionCreateRequest matchingSession =
        // request.studySessions().stream().filter(s->s.studyDetailId().equals(studyDetail.getId())).findFirst().get();
        //
        // studyDetail.updateSession(studyDetail.getStudy().getPeriod().getStartDate(),matchingSession.title(),matchingSession.description(),matchingSession.difficulty(),matchingSession.status());
        //
        //            updatedStudyDetails.add(studyDetail);
        //        }
        studyDetailRepository.saveAll(updatedStudyDetails);
    }
}
