package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.STUDY_NOT_FOUND;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.*;
import com.gdschongik.gdsc.domain.study.dto.request.StudySessionCreateRequest;
import com.gdschongik.gdsc.domain.study.dto.request.StudyUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.response.MentorStudyResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyStudentResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class MentorStudyService {

    private final MemberUtil memberUtil;
    private final StudyRepository studyRepository;
    private final StudyHistoryRepository studyHistoryRepository;
    private final StudyValidator studyValidator;
    private final StudyDetailRepository studyDetailRepository;
    private final StudyDetailValidator studyDetailValidator;

    @Transactional(readOnly = true)
    public List<MentorStudyResponse> getStudiesInCharge() {
        Member currentMember = memberUtil.getCurrentMember();
        List<Study> myStudies = studyRepository.findAllByMentor(currentMember);
        return myStudies.stream().map(MentorStudyResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<StudyStudentResponse> getStudyStudents(Long studyId) {
        Member currentMember = memberUtil.getCurrentMember();
        Study study =
                studyRepository.findById(studyId).orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

        studyValidator.validateStudyMentor(currentMember, study);
        List<StudyHistory> studyHistories = studyHistoryRepository.findByStudyId(studyId);

        return studyHistories.stream().map(StudyStudentResponse::from).toList();
    }

    // TODO session -> curriculum 변경
    @Transactional
    public void updateStudy(Long studyId, StudyUpdateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        studyValidator.validateStudyMentor(currentMember, study);

        List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyId(studyId);
        studyDetailValidator.validateUpdateStudyDetail(studyDetails, request.studySessions());

        study.update(request.notionLink(), request.introduction());
        studyRepository.save(study);
        log.info("[MentorStudyService] 스터디 기본 정보 수정 완료: studyId={}", studyId);

        Map<Long, StudySessionCreateRequest> requestMap = request.studySessions().stream()
                .collect(Collectors.toMap(StudySessionCreateRequest::studyDetailId, Function.identity()));

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
        studyDetailRepository.saveAll(updatedStudyDetails);
        log.info("[MentorStudyService] 스터디 상세정보 커리큘럼 작성 완료: studyDetailId={}", studyDetails);
    }
}
