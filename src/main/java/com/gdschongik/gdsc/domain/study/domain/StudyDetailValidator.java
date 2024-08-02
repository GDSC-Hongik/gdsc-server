package com.gdschongik.gdsc.domain.study.domain;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;

@DomainService
public class StudyDetailValidator {

    public void validateCancelStudyAssignment(Member currentMember, StudyDetail studyDetail) {
        validateMemberIsMentor(currentMember, studyDetail);
    }

    // 멘토가 아니라면 과제를 휴강처리 할 수 없다.
    private void validateMemberIsMentor(Member member, StudyDetail studyDetail) {
        if (!member.equals(studyDetail.getStudy().getMentor())) {
            throw new CustomException(ErrorCode.STUDY_DETAIL_NOT_MODIFIABLE_INVALID_ROLE);
        }
    }
}
