package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.request.AssignmentCreateRequest;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDateTime;

@DomainService
public class StudyDetailValidator {

    public void validateCancelStudyAssignment(Member member, StudyDetail studyDetail) {
        validateStudyMentorAuthorization(member, studyDetail);
    }

    public void validatePublishStudyAssignment(
            Member member, StudyDetail studyDetail, AssignmentCreateRequest request) {
        validateStudyMentorAuthorization(member, studyDetail);
        validateDeadLine(request.deadLine());
    }

    // 멘토가 아니라면 과제를 휴강처리 할 수 없다.
    private void validateStudyMentorAuthorization(Member member, StudyDetail studyDetail) {
        if (!member.getId().equals(studyDetail.getStudy().getMentor().getId())) {
            throw new CustomException(STUDY_DETAIL_NOT_MODIFIABLE_INVALID_ROLE);
        }
    }

    private void validateDeadLine(LocalDateTime deadline) {
        if (deadline.isBefore(LocalDateTime.now())) {
            throw new CustomException(ASSIGNMENT_DEADLINE_INVALID);
        }
    }
}
