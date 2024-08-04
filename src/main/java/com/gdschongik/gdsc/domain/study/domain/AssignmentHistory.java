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

    private long length;

    @Enumerated(EnumType.STRING)
    private AssignmentSubmissionStatus status;

    @Builder(access = AccessLevel.PRIVATE)
    private AssignmentHistory(
            StudyDetail studyDetail,
            Member member,
            String submissionLink,
            String commitHash,
            long length,
            AssignmentSubmissionStatus status) {
        this.studyDetail = studyDetail;
        this.member = member;
        this.submissionLink = submissionLink;
        this.commitHash = commitHash;
        this.length = length;
        this.status = status;
    }

    public static AssignmentHistory create(
            StudyDetail studyDetail,
            Member member,
            String submissionLink,
            String commitHash,
            long length,
            AssignmentSubmissionStatus status) {
        return AssignmentHistory.builder()
                .studyDetail(studyDetail)
                .member(member)
                .submissionLink(submissionLink)
                .commitHash(commitHash)
                .length(length)
                .status(status)
                .build();
    }
}
