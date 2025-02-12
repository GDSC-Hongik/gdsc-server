package com.gdschongik.gdsc.domain.studyv2.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;

@DomainService
public class StudyHistoryValidator {

    public void validateAppliedToStudy(long countStudyHistory, int studentCount) {
        if (countStudyHistory != studentCount) {
            throw new CustomException(STUDY_HISTORY_NOT_APPLIED_STUDENT_EXISTS);
        }
    }
}
