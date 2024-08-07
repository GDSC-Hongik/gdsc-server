package com.gdschongik.gdsc.domain.study.domain;

import com.gdschongik.gdsc.domain.common.model.BaseEntity;
import com.gdschongik.gdsc.domain.member.domain.Member;
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

    private Long contentLength;

    @Enumerated(EnumType.STRING)
    private AssignmentSubmissionStatus submissionStatus;

    @Enumerated(EnumType.STRING)
    private SubmissionFailureType submissionFailureType;

    @Builder(access = AccessLevel.PRIVATE)
    private AssignmentHistory(
            StudyDetail studyDetail,
            Member member,
            String submissionLink,
            String commitHash,
            Long contentLength,
            AssignmentSubmissionStatus submissionStatus) {
        this.studyDetail = studyDetail;
        this.member = member;
        this.submissionLink = submissionLink;
        this.commitHash = commitHash;
        this.contentLength = contentLength;
        this.submissionStatus = submissionStatus;
    }

    public static AssignmentHistory create(
            StudyDetail studyDetail, Member member, String submissionLink, String commitHash, Long contentLength) {
        return AssignmentHistory.builder()
                .studyDetail(studyDetail)
                .member(member)
                .submissionLink(submissionLink)
                .commitHash(commitHash)
                .contentLength(contentLength)
                .submissionStatus(AssignmentSubmissionStatus.PENDING)
                .build();
    }
}
