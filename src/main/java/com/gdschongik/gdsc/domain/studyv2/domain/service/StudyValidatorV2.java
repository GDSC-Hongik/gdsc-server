package com.gdschongik.gdsc.domain.studyv2.domain.service;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;

@DomainService
public class StudyValidatorV2 {

    public void validateStudyMentor(Member currentMember, StudyV2 study) {
        // 어드민인 경우 검증 통과
        if (currentMember.isAdmin()) {
            return;
        }

        // 멘토인지 검증
        if (!currentMember.isMentor()) {
            throw new CustomException(STUDY_ACCESS_NOT_ALLOWED);
        }

        // 해당 스터디의 담당 멘토인지 검증
        if (!currentMember.getId().equals(study.getMentor().getId())) {
            throw new CustomException(STUDY_MENTOR_INVALID);
        }
    }
}
