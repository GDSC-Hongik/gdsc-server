package com.gdschongik.gdsc.domain.study.application;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.StudyAchievementRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyAchievement;
import com.gdschongik.gdsc.domain.study.domain.StudyHistoryValidator;
import com.gdschongik.gdsc.domain.study.domain.StudyValidator;
import com.gdschongik.gdsc.domain.study.dto.request.OutstandingStudentRequest;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.util.List;
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
    private final StudyHistoryValidator studyHistoryValidator;
    private final StudyRepository studyRepository;
    private final StudyHistoryRepository studyHistoryRepository;
    private final StudyAchievementRepository studyAchievementRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void designateOutstandingStudent(Long studyId, OutstandingStudentRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        Study study = studyRepository.getById(studyId);
        Long countByStudyIdAndStudentIds =
                studyHistoryRepository.countByStudyIdAndStudentIds(studyId, request.studentIds());

        studyValidator.validateStudyMentor(currentMember, study);
        studyHistoryValidator.validateAppliedToStudy(
                countByStudyIdAndStudentIds, request.studentIds().size());

        List<Member> outstandingStudents = memberRepository.findAllById(request.studentIds());
        List<StudyAchievement> studyAchievements = outstandingStudents.stream()
                .map(member -> StudyAchievement.create(member, study, request.achievementType()))
                .toList();
        studyAchievementRepository.saveAll(studyAchievements);

        log.info(
                "[MentorStudyAchievementService] 우수 스터디원 지정: studyId={}, studentIds={}", studyId, request.studentIds());
    }

    @Transactional
    public void withdrawOutstandingStudent(Long studyId, OutstandingStudentRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        Study study = studyRepository.getById(studyId);
        Long countByStudyIdAndStudentIds =
                studyHistoryRepository.countByStudyIdAndStudentIds(studyId, request.studentIds());

        studyValidator.validateStudyMentor(currentMember, study);
        studyHistoryValidator.validateAppliedToStudy(
                countByStudyIdAndStudentIds, request.studentIds().size());

        studyAchievementRepository.deleteByStudyAndAchievementTypeAndMemberIds(
                studyId, request.achievementType(), request.studentIds());

        log.info(
                "[MentorStudyAchievementService] 우수 스터디원 철회: studyId={}, studentIds={}", studyId, request.studentIds());
    }
}
