package com.gdschongik.gdsc.domain.study.domain;

import com.gdschongik.gdsc.domain.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class StudyNotification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String link;

    @Builder(access = AccessLevel.PRIVATE)
    public StudyNotification(Study study, String title, String link) {
        this.study = study;
        this.title = title;
        this.link = link;
    }

    public static StudyNotification createStudyNotification(Study study, String title, String link) {
        return StudyNotification.builder().study(study).title(title).link(link).build();
    }

    public void update(String title, String link) {
        this.title = title;
        this.link = link;
    }
}
