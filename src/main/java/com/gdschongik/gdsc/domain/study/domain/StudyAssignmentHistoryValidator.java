package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDateTime;

@DomainService
public class StudyAssignmentHistoryValidator {

    /**
     * 과제 제출 전, 빈 제출이력 생성이 가능한지 검증합니다.
     */
    public void validateCreateAssignmentHistory(boolean isAppliedToStudy, LocalDateTime now, StudyDetail studyDetail) {
        if (!isAppliedToStudy) {
            throw new CustomException(ASSIGNMENT_STUDY_NOT_APPLIED);
        }

        // TODO: 과제 시작기간 이전에 생성하는 경우도 고려해야 함

        studyDetail.validateAssignmentSubmittable(now);
    }

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
