package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.STUDY_MENTOR_INVALID;
import static com.gdschongik.gdsc.global.exception.ErrorCode.STUDY_MENTOR_IS_UNAUTHORIZED;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;

@DomainService
public class StudyValidator {
    public void validateStudyMentor(Member currentMember, Long mentorId) {
        if (currentMember.isGuest()) {
            throw new CustomException(STUDY_MENTOR_IS_UNAUTHORIZED);
        }

        if (!currentMember.getId().equals(mentorId)) {
            throw new CustomException(STUDY_MENTOR_INVALID);
        }
    }
}
