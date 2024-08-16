package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.STUDY_MENTOR_INVALID;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;

@DomainService
public class StudyValidator {
    public void validateStudyMentor(Member currentMember, Study study) {
        if (!currentMember.isAdmin()
                && !currentMember.getId().equals(study.getMentor().getId())) {
            throw new CustomException(STUDY_MENTOR_INVALID);
        }
    }
}
