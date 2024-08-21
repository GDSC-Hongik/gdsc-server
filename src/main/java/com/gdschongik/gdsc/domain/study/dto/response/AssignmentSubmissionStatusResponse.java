package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import com.gdschongik.gdsc.domain.study.domain.vo.Assignment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 응답용 enum
@Getter
@AllArgsConstructor
public enum AssignmentSubmissionStatusResponse {
    SUCCESS("제출완료"),
    FAILURE("제출실패"),
    NOT_SUBMITTED("미제출"),
    // 제출기한이 아님 or 과제가 개설되지 않음 or 과제 휴강
    CANNOT_SUBMIT("제출불가");

    private final String value;

    public static AssignmentSubmissionStatusResponse of(
            Assignment assignment, AssignmentHistory assignmentHistory, LocalDateTime now) {
        // 과제를 제출완료한 경우
        if (assignmentHistory != null && assignmentHistory.isSubmitted() && assignmentHistory.isSuccess()) {
            return SUCCESS;
        }
        // 과제를 제출실패한 경우
        if (assignmentHistory != null && assignmentHistory.isSubmitted() && assignmentHistory.isFailure()) {
            return FAILURE;
        }

        // 과제가 개설되지 않음 or 제출기한이 아님 or 과제 휴강
        if (assignment.isNone() || now.isAfter(assignment.getDeadline()) || assignment.isCancelled()) {
            return CANNOT_SUBMIT;
        }

        return NOT_SUBMITTED;
    }
}
