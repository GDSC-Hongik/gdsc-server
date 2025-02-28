package com.gdschongik.gdsc.domain.studyv2.dto.dto;

import com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionStatus;
import com.gdschongik.gdsc.domain.study.domain.SubmissionFailureType;
import java.time.LocalDateTime;

public record AssignmentHistoryDto(
        Long assignmentHistoryId,
        AssignmentSubmissionStatus submissionStatus,
        SubmissionFailureType submissionFailureType,
        Integer contentLength,
        String submissionLink,
        String commitHash,
        LocalDateTime committedAt,
        Long studySessionId,
        Long memberId) {}
