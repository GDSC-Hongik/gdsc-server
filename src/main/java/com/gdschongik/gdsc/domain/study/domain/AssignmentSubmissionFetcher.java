package com.gdschongik.gdsc.domain.study.domain;

import com.gdschongik.gdsc.global.exception.CustomException;

@FunctionalInterface
public interface AssignmentSubmissionFetcher {

    AssignmentSubmission fetch() throws CustomException;
}
