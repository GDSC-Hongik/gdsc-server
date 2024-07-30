package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.ASSIGNMENT_NOT_FOUND;

import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.request.AssignmentCreateRequest;
import com.gdschongik.gdsc.domain.study.domain.vo.Assignment;
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

    private final StudyDetailRepository studyDetailRepository;

    @Transactional
    public void updateStudyAssignment(Long studyDetailId, AssignmentCreateRequest request) {
        StudyDetail studyDetail = studyDetailRepository
                .findById(studyDetailId)
                .orElseThrow(() -> new CustomException(ASSIGNMENT_NOT_FOUND));
        Assignment assignment = studyDetail.getAssignment();

        assignment.update(request.title(), request.deadLine(), request.descriptionNotionLink());

        log.info("[StudyMentorService] 과제 개설/수정 완료: studyDetailId={}", studyDetailId);
    }
}
