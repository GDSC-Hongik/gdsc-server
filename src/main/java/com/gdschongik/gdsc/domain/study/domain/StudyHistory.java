package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.domain.study.domain.StudyHistoryStatus.*;

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
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "study_id"})})
public class StudyHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    private String repositoryLink;

    @Comment("수료 상태")
    @Enumerated(EnumType.STRING)
    private StudyHistoryStatus studyHistoryStatus;

    @Builder(access = AccessLevel.PRIVATE)
    private StudyHistory(Member student, Study study) {
        this.student = student;
        this.study = study;
        this.studyHistoryStatus = NONE;
    }

    public static StudyHistory create(Member student, Study study) {
        return StudyHistory.builder().student(student).study(study).build();
    }

    @PreRemove
    private void preRemove() {
        registerEvent(new StudyApplyCanceledEvent(this.study.getId(), this.student.getId()));
    }

    /**
     * 레포지토리 링크를 업데이트합니다.
     */
    public void updateRepositoryLink(String repositoryLink) {
        this.repositoryLink = repositoryLink;
    }

    /**
     * 스터디 수료
     */
    public void complete() {
        studyHistoryStatus = COMPLETED;
        registerEvent(new StudyHistoryCompletedEvent(this.id));
    }

    /**
     * 스터디 수료 철회
     */
    public void withdrawCompletion() {
        studyHistoryStatus = NONE;
    }

    // 데이터 전달 로직
    public boolean isWithinApplicationAndCourse() {
        return study.isWithinApplicationAndCourse();
    }

    public boolean isCompleted() {
        return studyHistoryStatus == COMPLETED;
    }
}
