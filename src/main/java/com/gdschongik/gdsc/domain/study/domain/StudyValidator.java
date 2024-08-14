package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.STUDY_MENTOR_INVALID;

import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;

@DomainService
public class StudyValidator {
    public void validateStudyMentor(Long currentMemberId, Long mentorId) {
        if (!currentMemberId.equals(mentorId)) {
            throw new CustomException(STUDY_MENTOR_INVALID);
        }
    }
}
