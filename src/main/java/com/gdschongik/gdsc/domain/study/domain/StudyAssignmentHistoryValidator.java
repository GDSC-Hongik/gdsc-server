package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDateTime;

@DomainService
public class StudyAssignmentHistoryValidator {

    /**
     * 채점을 수행하기 전, 과제 제출이 가능한지 검증합니다.
     */
    public void validateSubmitAvailable(
            boolean isAppliedToStudy, LocalDateTime now, AssignmentHistory assignmentHistory) {
        if (!isAppliedToStudy) {
            throw new CustomException(ASSIGNMENT_STUDY_NOT_APPLIED);
        }

        StudyDetail studyDetail = assignmentHistory.getStudyDetail();
        studyDetail.validateAssignmentSubmittable(now);
    }
}
