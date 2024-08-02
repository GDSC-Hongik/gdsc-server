package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.ASSIGNMENT_DEADLINE_INVALID;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.request.AssignmentCreateRequest;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import java.time.LocalDateTime;

@DomainService
public class StudyDetailValidator {

    public void validateCancelStudyAssignment(Member currentMember, StudyDetail studyDetail) {
        validateMemberIsMentor(currentMember, studyDetail);
    }

    public void validatePublishStudyAssignment(
            Member member, StudyDetail studyDetail, AssignmentCreateRequest request) {
        if (!member.equals(studyDetail.getStudy().getMentor())) {
            throw new CustomException(ErrorCode.STUDY_DETAIL_NOT_MODIFIABLE_INVALID_ROLE);
        }

        if (validateDeadLine(request.deadLine())) {
            throw new CustomException(ASSIGNMENT_DEADLINE_INVALID);
        }
    }

    // 멘토가 아니라면 과제를 휴강처리 할 수 없다.
    private void validateMemberIsMentor(Member member, StudyDetail studyDetail) {
        if (!member.equals(studyDetail.getStudy().getMentor())) {
            throw new CustomException(ErrorCode.STUDY_DETAIL_NOT_MODIFIABLE_INVALID_ROLE);
        }
    }

    private boolean validateDeadLine(LocalDateTime deadline) {
        return deadline.isBefore(LocalDateTime.now());
    }
}
