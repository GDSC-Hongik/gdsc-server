package com.gdschongik.gdsc.domain.studyv2.dto.dto;

import com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionStatus;
import com.gdschongik.gdsc.domain.study.domain.SubmissionFailureType;
import com.gdschongik.gdsc.domain.studyv2.domain.AssignmentHistoryV2;
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
        Long memberId) {
    public static AssignmentHistoryDto from(AssignmentHistoryV2 assignmentHistory) {
        return new AssignmentHistoryDto(
                assignmentHistory.getId(),
                assignmentHistory.getSubmissionStatus(),
                assignmentHistory.getSubmissionFailureType(),
                assignmentHistory.getContentLength(),
                assignmentHistory.getSubmissionLink(),
                assignmentHistory.getCommitHash(),
                assignmentHistory.getCommittedAt(),
                assignmentHistory.getStudySession().getId(),
                assignmentHistory.getMember().getId());
    }
}
