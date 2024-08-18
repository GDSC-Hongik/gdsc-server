package com.gdschongik.gdsc.domain.study.domain.vo;

import com.gdschongik.gdsc.domain.study.domain.AssignmentSubmission;
import com.gdschongik.gdsc.global.exception.CustomException;

@FunctionalInterface
public interface AssignmentSubmissionFetcher {

    AssignmentSubmission fetch() throws CustomException;
}
