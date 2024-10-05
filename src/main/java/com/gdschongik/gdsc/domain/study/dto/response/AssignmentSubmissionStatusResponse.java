package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AssignmentSubmissionStatusResponse {
    NOT_SUBMITTED("미제출"),
    FAILURE("제출 실패"),
    SUCCESS("제출 성공"),
    CANCELLED("휴강");

    private final String value;

    public static AssignmentSubmissionStatusResponse from(AssignmentHistory assignmentHistory) {
        if (assignmentHistory == null) {
            return NOT_SUBMITTED;
        }
        if (assignmentHistory.isSuccess()) {
            return SUCCESS;
        } else {
            return FAILURE;
        }
    }

    public static AssignmentSubmissionStatusResponse getCancelled() {
        return CANCELLED;
    }
}
