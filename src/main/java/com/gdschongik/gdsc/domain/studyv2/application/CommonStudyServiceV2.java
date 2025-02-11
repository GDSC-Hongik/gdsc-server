package com.gdschongik.gdsc.domain.studyv2.application;

import com.gdschongik.gdsc.domain.studyv2.dao.StudySessionV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.StudySessionV2;
import com.gdschongik.gdsc.domain.studyv2.dto.response.AssignmentResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonStudyServiceV2 {

    private final StudySessionV2Repository studySessionV2Repository;

    @Transactional(readOnly = true)
    public AssignmentResponse getAssignment(Long studySessionId) {
        StudySessionV2 studySessionV2 = studySessionV2Repository
                .findById(studySessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_SESSION_NOT_FOUND));
        return AssignmentResponse.from(studySessionV2);
    }
}
