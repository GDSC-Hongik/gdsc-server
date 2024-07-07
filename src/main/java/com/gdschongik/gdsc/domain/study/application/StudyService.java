package com.gdschongik.gdsc.domain.study.application;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.dto.request.StudyCreateRequest;
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
@Transactional(readOnly = true)
public class StudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyDetailRepository studyDetailRepository;

    @Transactional
    public void createStudyAndStudyDetail(StudyCreateRequest request) {
        final Member mentor = getMemberById(request.mentorId());

        Study study = StudyDomainFactory.createNewStudy(request, mentor);
        final Study savedStudy = studyRepository.save(study);

        List<StudyDetail> studyDetails = createNoneStudyDetail(savedStudy);
        studyDetailRepository.saveAll(studyDetails);

        log.info(
                "[StudyService] 스터디 생성: studyId = {}, mentorId={}, "
                        + "academicYear={}, semesterType={}, applicationStartDate={}, applicationEndDate={}",
                study.getId(),
                request.mentorId(),
                request.academicYear(),
                request.semesterType(),
                request.applicationStartDate());
    }

    private List<StudyDetail> createNoneStudyDetail(Study study) {
        List<StudyDetail> studyDetails = new ArrayList<>();

        for (long i = 1; i <= study.getTotalWeek(); i++) {
            studyDetails.add(StudyDomainFactory.createNoneStudyDetail(study, i));
        }
        return studyDetails;
    }

    private Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
