package com.gdschongik.gdsc.domain.study.application;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.StudyHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.gdschongik.gdsc.domain.study.domain.StudyHistoryValidator;
import com.gdschongik.gdsc.domain.study.dto.response.StudyResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.util.MemberUtil;
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

    private final MemberUtil memberUtil;
    private final StudyRepository studyRepository;
    private final StudyHistoryRepository studyHistoryRepository;
    private final StudyHistoryValidator studyHistoryValidator;

    public List<StudyResponse> getAllApplicableStudies() {
        return studyRepository.findAll().stream()
                .filter(Study::isApplicable)
                .map(StudyResponse::from)
                .toList();
    }

    @Transactional
    public void applyStudy(Long studyId) {
        Study study =
                studyRepository.findById(studyId).orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));
        Member currentMember = memberUtil.getCurrentMember();

        List<StudyHistory> currentMemberStudyHistories = studyHistoryRepository.findAllByMentee(currentMember);

        studyHistoryValidator.validateApplyStudy(study, currentMemberStudyHistories);

        StudyHistory studyHistory = StudyHistory.create(currentMember, study);
        studyHistoryRepository.save(studyHistory);

        log.info("[StudyService] 스터디 수강신청: studyHistoryId={}", studyHistory.getId());
    }
}
