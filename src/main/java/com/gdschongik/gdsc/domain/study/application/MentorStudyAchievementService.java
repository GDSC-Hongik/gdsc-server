package com.gdschongik.gdsc.domain.study.application;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyValidator;
import com.gdschongik.gdsc.domain.study.dto.request.OutstandingStudentRequest;
import com.gdschongik.gdsc.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorStudyAchievementService {

    private final MemberUtil memberUtil;
    private final StudyValidator studyValidator;
    private final StudyRepository studyRepository;

    @Transactional
    public void designateOutstandingStudent(Long studyId, OutstandingStudentRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        Study study = studyRepository.getById(studyId);
        studyValidator.validateStudyMentor(currentMember, study);

        // todo: 지정 로직 추가
        log.info(
                "[MentorStudyAchievementService] 우수 스터디원 지정: studyId={}, studentIds={}", studyId, request.studentIds());
    }

    @Transactional
    public void withdrawOutstandingStudent(Long studyId, OutstandingStudentRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        Study study = studyRepository.getById(studyId);
        studyValidator.validateStudyMentor(currentMember, study);

        // todo: 철회 로직 추가
        log.info(
                "[MentorStudyAchievementService] 우수 스터디원 철회: studyId={}, studentIds={}", studyId, request.studentIds());
    }
}
