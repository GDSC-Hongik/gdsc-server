package com.gdschongik.gdsc.domain.study.application;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.dto.request.StudyCreateRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public void openStudy(StudyCreateRequest request) {
        final Member mentor = getMemberById(request.mentorId());

        Study study = createStudy(request, mentor);
        final Study savedStudy = studyRepository.save(study);

        List<StudyDetail> studyDetails = createStudyDetail(savedStudy);
        studyDetailRepository.saveAll(studyDetails);

        log.info(
                "[StudyService] 스터디 생성: mentorId={}, "
                        + "academicYear={}, semesterType={}, applicationStartDate={}, "
                        + "applicationEndDate={}, totalWeek={}, startDate={}, dayOfWeek={}, studyType={}",
                request.mentorId(),
                request.academicYear(),
                request.semesterType(),
                request.applicationStartDate(),
                request.applicationEndDate(),
                request.totalWeek(),
                request.startDate(),
                request.dayOfWeek(),
                request.studyType());
    }

    private Study createStudy(StudyCreateRequest request, Member mentor) {
        LocalDate endDate = request.startDate().plusDays(request.totalWeek() * 7 - 1);
        return Study.createStudy(
                request.academicYear(),
                request.semesterType(),
                mentor,
                Period.createPeriod(request.startDate().atStartOfDay(), endDate.atTime(LocalTime.MAX)),
                Period.createPeriod(
                        request.applicationStartDate().atStartOfDay(),
                        request.applicationEndDate().atTime(LocalTime.MAX)),
                request.totalWeek(),
                request.studyType(),
                request.dayOfWeek());
    }

    private List<StudyDetail> createStudyDetail(Study study) {
        List<StudyDetail> studyDetails = new ArrayList<>();

        for (long i = 1; i <= study.getTotalWeek(); i++) {
            LocalDateTime startDate = study.getPeriod().getStartDate().plusDays((i - 1) * 7);
            LocalDateTime endDate = startDate.plusDays(6).toLocalDate().atTime(LocalTime.MAX);
            studyDetails.add(StudyDetail.createStudyDetail(study, i, Period.createPeriod(startDate, endDate)));
        }
        return studyDetails;
    }

    private Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
