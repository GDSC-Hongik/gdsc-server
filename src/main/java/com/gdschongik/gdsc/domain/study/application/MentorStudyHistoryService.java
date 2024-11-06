package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.StudyHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.gdschongik.gdsc.domain.study.domain.StudyHistoryValidator;
import com.gdschongik.gdsc.domain.study.domain.StudyValidator;
import com.gdschongik.gdsc.domain.study.dto.request.StudyCompletionRequest;
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
public class MentorStudyHistoryService {

    private final MemberUtil memberUtil;
    private final StudyValidator studyValidator;
    private final StudyHistoryValidator studyHistoryValidator;
    private final StudyRepository studyRepository;
    private final StudyHistoryRepository studyHistoryRepository;

    @Transactional
    public void completeStudy(Long studyId, StudyCompletionRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        List<StudyHistory> studyHistories = studyHistoryRepository.findAllById(request.studentIds());

        studyValidator.validateStudyMentor(currentMember, study);
        studyHistoryValidator.validateAppliedToStudy(
                studyHistories.size(), request.studentIds().size());

        studyHistories.forEach(StudyHistory::complete);

        log.info("[MentorStudyHistoryService] 스터디 수료 처리: studyId={}, studentIds={}", studyId, request.studentIds());
    }

    @Transactional
    public void withdrawStudyCompletion(Long studyId, StudyCompletionRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        List<StudyHistory> studyHistories = studyHistoryRepository.findAllById(request.studentIds());

        studyValidator.validateStudyMentor(currentMember, study);
        studyHistoryValidator.validateAppliedToStudy(
                studyHistories.size(), request.studentIds().size());

        studyHistories.forEach(StudyHistory::withdrawCompletion);

        log.info("[MentorStudyHistoryService] 스터디 수료 철회: studyId={}, studentIds={}", studyId, request.studentIds());
    }
}
