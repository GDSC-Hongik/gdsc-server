package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionStatus.*;
import static com.gdschongik.gdsc.domain.study.domain.SubmissionFailureType.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.BaseEntity;
import com.gdschongik.gdsc.domain.member.domain.Member;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssignmentHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_detail_id")
    private StudyDetail studyDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String submissionLink;

    private String commitHash;

    private Integer contentLength;

    private LocalDateTime committedAt;

    @Enumerated(EnumType.STRING)
    private AssignmentSubmissionStatus submissionStatus;

    @Enumerated(EnumType.STRING)
    private SubmissionFailureType submissionFailureType;

    @Builder(access = AccessLevel.PRIVATE)
    private AssignmentHistory(
            StudyDetail studyDetail,
            Member member,
            AssignmentSubmissionStatus submissionStatus,
            SubmissionFailureType submissionFailureType) {
        this.studyDetail = studyDetail;
        this.member = member;
        this.submissionStatus = submissionStatus;
        this.submissionFailureType = submissionFailureType;
    }

    public static AssignmentHistory create(StudyDetail studyDetail, Member member) {
        return AssignmentHistory.builder()
                .studyDetail(studyDetail)
                .member(member)
                .submissionStatus(FAILURE)
                .submissionFailureType(NOT_SUBMITTED)
                .build();
    }

    // 데이터 조회 로직

    public boolean isSubmitted() {
        return submissionFailureType != NOT_SUBMITTED;
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
