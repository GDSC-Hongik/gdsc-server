package com.gdschongik.gdsc.domain.studyv2.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dto.request.OutstandingStudentRequest;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyAchievementV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyAchievementV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyAchievementValidatorV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryValidatorV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyValidatorV2;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorStudyAchievementServiceV2 {

    private final MemberUtil memberUtil;
    private final StudyValidatorV2 studyValidator;
    private final StudyHistoryValidatorV2 studyHistoryValidator;
    private final StudyAchievementValidatorV2 studyAchievementValidator;
    private final StudyV2Repository studyRepository;
    private final StudyHistoryV2Repository studyHistoryRepository;
    private final StudyAchievementV2Repository studyAchievementRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void designateOutstandingStudent(Long studyId, OutstandingStudentRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyV2 study = studyRepository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        studyValidator.validateStudyMentor(currentMember, study);

        long studyHistoryCount = studyHistoryRepository.countByStudyIdAndStudentIds(studyId, request.studentIds());
        studyHistoryValidator.validateAppliedToStudy(
                studyHistoryCount, request.studentIds().size());

        long studyAchievementsAlreadyExistCount =
                studyAchievementRepository.countByStudyIdAndAchievementTypeAndStudentIds(
                        studyId, request.achievementType(), request.studentIds());
        studyAchievementValidator.validateDesignateOutstandingStudent(studyAchievementsAlreadyExistCount);

        List<Member> outstandingStudents = memberRepository.findAllById(request.studentIds());
        List<StudyAchievementV2> studyAchievements = outstandingStudents.stream()
                .map(member -> StudyAchievementV2.create(request.achievementType(), member, study))
                .toList();
        studyAchievementRepository.saveAll(studyAchievements);

        log.info(
                "[MentorStudyAchievementServiceV2] 우수 스터디원 지정: studyId={}, studentIds={}",
                studyId,
                request.studentIds());
    }

    @Transactional
    public void withdrawOutstandingStudent(Long studyId, OutstandingStudentRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyV2 study = studyRepository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        studyValidator.validateStudyMentor(currentMember, study);

        long studyHistoryCount = studyHistoryRepository.countByStudyIdAndStudentIds(studyId, request.studentIds());
        studyHistoryValidator.validateAppliedToStudy(
                studyHistoryCount, request.studentIds().size());

        studyAchievementRepository.deleteByStudyAndAchievementTypeAndMemberIds(
                studyId, request.achievementType(), request.studentIds());

        log.info(
                "[MentorStudyAchievementServiceV2] 우수 스터디원 철회: studyId={}, studentIds={}",
                studyId,
                request.studentIds());
    }
}
