package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyMentorService {

    private final MemberUtil memberUtil;
    private final StudyDetailRepository studyDetailRepository;
    private final StudyDetailValidator studyDetailValidator;

    @Transactional
    public void cancelStudyAssignment(Long studyDetailId) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyDetail studyDetail = studyDetailRepository
                .findById(studyDetailId)
                .orElseThrow(() -> new CustomException(STUDY_DETAIL_NOT_FOUND));

        studyDetailValidator.validateCancelStudyAssignment(currentMember, studyDetail);

        studyDetail.cancelAssignment();

        log.info("[StudyMentorService] 과제 휴강 처리: studyDetailId={}", studyDetail.getId());
    }
}
