package com.gdschongik.gdsc.domain.studyv2.domain;

import static com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionStatus.*;
import static com.gdschongik.gdsc.domain.study.domain.SubmissionFailureType.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionStatus;
import com.gdschongik.gdsc.domain.study.domain.SubmissionFailureType;
import com.gdschongik.gdsc.global.exception.CustomException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "assignment_history_v2",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"study_session_v2_id", "member_id"})})
public class AssignmentHistoryV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_history_v2_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private AssignmentSubmissionStatus submissionStatus;

    @Enumerated(EnumType.STRING)
    private SubmissionFailureType submissionFailureType;

    private Integer contentLength;

    @Column(columnDefinition = "TEXT")
    private String submissionLink;

    private String commitHash;

    private LocalDateTime committedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_session_v2_id")
    private StudySessionV2 studySession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder(access = AccessLevel.PRIVATE)
    private AssignmentHistoryV2(
            AssignmentSubmissionStatus submissionStatus,
            SubmissionFailureType submissionFailureType,
            StudySessionV2 studySession,
            Member member) {
        this.submissionStatus = submissionStatus;
        this.submissionFailureType = submissionFailureType;
        this.studySession = studySession;
        this.member = member;
    }

    public static AssignmentHistoryV2 create(StudySessionV2 studySession, Member member) {
        return AssignmentHistoryV2.builder()
                .submissionStatus(FAILURE)
                .submissionFailureType(NOT_SUBMITTED)
                .studySession(studySession)
                .member(member)
                .build();
    }

    // 데이터 조회 로직

    public boolean isSubmitted() {
        return submissionFailureType != NOT_SUBMITTED;
    }

    public boolean isSucceeded() {
        return submissionStatus == SUCCESS;
    }

    // 데이터 변경 로직

    public void success(String submissionLink, String commitHash, Integer contentLength, LocalDateTime committedAt) {
        this.submissionLink = submissionLink;
        this.commitHash = commitHash;
        this.contentLength = contentLength;
        this.committedAt = committedAt;
        this.submissionStatus = SUCCESS;
        this.submissionFailureType = NONE;
    }

    public void fail(SubmissionFailureType submissionFailureType) {
        if (submissionFailureType == NOT_SUBMITTED || submissionFailureType == NONE) {
            throw new CustomException(ASSIGNMENT_INVALID_FAILURE_TYPE);
        }
        this.submissionLink = null;
        this.commitHash = null;
        this.contentLength = null;
        this.committedAt = null;
        this.submissionStatus = FAILURE;
        this.submissionFailureType = submissionFailureType;
    }
}
