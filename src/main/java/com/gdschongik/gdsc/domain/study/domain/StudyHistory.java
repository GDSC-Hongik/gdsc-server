package com.gdschongik.gdsc.domain.study.domain;

import com.gdschongik.gdsc.domain.common.model.BaseEntity;
import com.gdschongik.gdsc.domain.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder(access = AccessLevel.PRIVATE)
    private StudyHistory(Member student, Study study) {
        this.student = student;
        this.study = study;
    }

    public static StudyHistory create(Member student, Study study) {
        return StudyHistory.builder().student(student).study(study).build();
    }

    /**
     * 레포지토리 링크를 업데이트합니다.
     */
    public void updateRepositoryLink(String repositoryLink) {
        this.repositoryLink = repositoryLink;
    }

    // 데이터 전달 로직
    public boolean isStudyOngoing() {
        return study.isStudyOngoing();
    }
}
