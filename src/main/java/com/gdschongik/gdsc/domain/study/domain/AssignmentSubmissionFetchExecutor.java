package com.gdschongik.gdsc.domain.study.domain;

import com.gdschongik.gdsc.global.exception.CustomException;

@FunctionalInterface
public interface AssignmentSubmissionFetchExecutor {
    AssignmentSubmission execute(String repo, int week) throws CustomException;
}
