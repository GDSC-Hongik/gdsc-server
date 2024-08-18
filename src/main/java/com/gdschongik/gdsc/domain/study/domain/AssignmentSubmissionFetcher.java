package com.gdschongik.gdsc.domain.study.domain;

import com.gdschongik.gdsc.global.exception.CustomException;

public record AssignmentSubmissionFetcher(String repo, int week, AssignmentSubmissionFetchExecutor fetchExecutor) {
    public AssignmentSubmission fetch() throws CustomException {
        return fetchExecutor.execute(repo, week);
    }
}
