package com.gdschongik.gdsc.domain.study.application;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.dto.request.StudyCreateRequest;
import com.gdschongik.gdsc.domain.study.dto.response.StudyResponse;
import com.gdschongik.gdsc.domain.study.factory.StudyDomainFactory;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminStudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyDetailRepository studyDetailRepository;
    private final StudyDomainFactory studyDomainFactory;

    @Transactional
    public void createStudyAndStudyDetail(StudyCreateRequest request) {
        // TODO: 멘토 권한 부여
        final Member mentor = getMemberById(request.mentorId());

        Study study = studyDomainFactory.createNewStudy(request, mentor);
        final Study savedStudy = studyRepository.save(study);

        // TODO: 레포지토리 분리 (DDD 적용)
        List<StudyDetail> studyDetails = createNoneStudyDetail(savedStudy);
        studyDetailRepository.saveAll(studyDetails);

        log.info("[AdminStudyService] 스터디 생성: studyId = {}", study.getId());
    }

    private List<StudyDetail> createNoneStudyDetail(Study study) {
        List<StudyDetail> studyDetails = new ArrayList<>();

        for (long i = 1; i <= study.getTotalWeek(); i++) {
            studyDetails.add(studyDomainFactory.createNoneStudyDetail(study, i));
        }
        return studyDetails;
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<StudyResponse> getAllStudies() {
        return studyRepository.findAll().stream().map(StudyResponse::from).toList();
    }
}
