package com.gdschongik.gdsc.domain.study.domain;

import com.gdschongik.gdsc.global.annotation.DomainService;
import java.time.LocalDateTime;

@DomainService
public class StudyAssignmentHistoryValidator {

    /**
     * 과제 제출 전, 빈 제출이력 생성이 가능한지 검증합니다.
     */
    public void validateCreateAssignmentHistory(LocalDateTime now, StudyDetail studyDetail) {
        studyDetail.validateAssignmentSubmittable(now);
    }

    /**
     * 채점을 수행하기 전, 과제 제출이 가능한지 검증합니다.
     */
    public void validateSubmit(LocalDateTime now, AssignmentHistory assignmentHistory) {
        StudyDetail studyDetail = assignmentHistory.getStudyDetail();
        studyDetail.validateAssignmentSubmittable(now);
    }
}
