package com.gdschongik.gdsc.domain.studyv2.dto.response;

import com.gdschongik.gdsc.domain.studyv2.domain.AssignmentHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudySessionV2;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AssignmentSubmissionStatusResponse {
    NOT_SUBMITTED("미제출"),
    FAILURE("제출 실패"),
    SUCCESS("제출 성공");

    private final String value;

    public static AssignmentSubmissionStatusResponse of(
            AssignmentHistoryV2 assignmentHistoryV2, StudySessionV2 studySessionV2) {
        if (assignmentHistoryV2 == null) {
            return NOT_SUBMITTED;
        }
        if (assignmentHistoryV2.isSucceeded()) {
            return SUCCESS;
        } else {
            return FAILURE;
        }
    }
}
