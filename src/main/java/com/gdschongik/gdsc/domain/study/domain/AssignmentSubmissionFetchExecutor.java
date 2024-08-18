package com.gdschongik.gdsc.domain.study.domain;

@FunctionalInterface
public interface AssignmentSubmissionFetchExecutor {

    AssignmentSubmission execute(String repo, int week);
}
