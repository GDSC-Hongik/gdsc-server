package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.domain.study.domain.SubmissionFailureType.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;

@DomainService
public class AssignmentHistoryGrader {

    public static final int MINIMUM_ASSIGNMENT_CONTENT_LENGTH = 300;

    public void judge(AssignmentSubmissionFetcher assignmentSubmissionFetcher, AssignmentHistory assignmentHistory) {
        try {
            AssignmentSubmission assignmentSubmission = assignmentSubmissionFetcher.fetch();
            judgeAssignmentSubmission(assignmentSubmission, assignmentHistory);
        } catch (CustomException e) {
            SubmissionFailureType failureType = translateException(e);
            assignmentHistory.fail(failureType);
        }
    }

    private void judgeAssignmentSubmission(
            AssignmentSubmission assignmentSubmission, AssignmentHistory assignmentHistory) {
        if (assignmentSubmission.contentLength() < MINIMUM_ASSIGNMENT_CONTENT_LENGTH) {
            assignmentHistory.fail(WORD_COUNT_INSUFFICIENT);
            return;
        }

        assignmentHistory.success(
                assignmentSubmission.url(),
                assignmentSubmission.commitHash(),
                assignmentSubmission.contentLength(),
                assignmentSubmission.committedAt());
    }

    private SubmissionFailureType translateException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        if (errorCode == GITHUB_CONTENT_NOT_FOUND) {
            return LOCATION_UNIDENTIFIABLE;
        }

        return UNKNOWN;
    }
}
