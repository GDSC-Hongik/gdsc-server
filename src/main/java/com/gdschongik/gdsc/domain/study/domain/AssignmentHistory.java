package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionStatus.*;

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

    private Long contentLength;

    private LocalDateTime committedAt;

    @Enumerated(EnumType.STRING)
    private AssignmentSubmissionStatus submissionStatus;

    @Enumerated(EnumType.STRING)
    private SubmissionFailureType submissionFailureType;

    @Builder(access = AccessLevel.PRIVATE)
    private AssignmentHistory(
            StudyDetail studyDetail,
            Member member,
            AssignmentSubmissionStatus submissionStatus) {
        this.studyDetail = studyDetail;
        this.member = member;
        this.submissionStatus = submissionStatus;
    }

    public static AssignmentHistory create(StudyDetail studyDetail, Member member) {
        return AssignmentHistory.builder()
                .studyDetail(studyDetail)
                .member(member)
                .submissionStatus(FAILURE)
                .build();
    }

    // 데이터 조회 로직

    public boolean isSubmitted() {
        return submissionStatus == SUCCESS || submissionStatus == FAILURE;
    }

    // 데이터 변경 로직

    public void success(String submissionLink, String commitHash, Long contentLength, LocalDateTime committedAt) {
        this.submissionLink = submissionLink;
        this.commitHash = commitHash;
        this.contentLength = contentLength;
        this.submissionStatus = SUCCESS;
        this.committedAt = committedAt;
    }

    public void fail(SubmissionFailureType submissionFailureType) {
        this.submissionStatus = FAILURE;
        this.submissionFailureType = submissionFailureType;
        this.committedAt = null;
    }
}
