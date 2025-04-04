package com.gdschongik.gdsc.domain.studyv2.domain.service;

import static com.gdschongik.gdsc.domain.study.domain.SubmissionFailureType.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.study.domain.AssignmentSubmission;
import com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionFetcher;
import com.gdschongik.gdsc.domain.study.domain.SubmissionFailureType;
import com.gdschongik.gdsc.domain.studyv2.domain.AssignmentHistoryV2;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@DomainService
public class AssignmentHistoryGraderV2 {

    private static final int MINIMUM_ASSIGNMENT_CONTENT_LENGTH = 300;

    public void judge(AssignmentSubmissionFetcher assignmentSubmissionFetcher, AssignmentHistoryV2 assignmentHistory) {
        try {
            AssignmentSubmission assignmentSubmission = assignmentSubmissionFetcher.fetch();
            judgeAssignmentSubmission(assignmentSubmission, assignmentHistory);
        } catch (CustomException e) {
            SubmissionFailureType failureType = translateException(e);
            assignmentHistory.fail(failureType);
        }
    }

    private void judgeAssignmentSubmission(
            AssignmentSubmission assignmentSubmission, AssignmentHistoryV2 assignmentHistory) {
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

        log.warn("[AssignmentHistoryGrader] 과제 제출정보 조회 중 알 수 없는 오류 발생: {}", e.getMessage());

        return UNKNOWN;
    }
}
