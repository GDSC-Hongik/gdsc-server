package com.gdschongik.gdsc.domain.study.domain;

public record AssignmentSubmissionFetcher(String repo, int week, AssignmentSubmissionFetchExecutor fetchExecutor) {
    public AssignmentSubmission fetch() {
        return fetchExecutor.execute(repo, week);
    }
}
