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
public class StudyAnnouncement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_announcement_id")
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @Builder(access = AccessLevel.PRIVATE)
    public StudyAnnouncement(String title, String link, Study study) {
        this.title = title;
        this.link = link;
        this.study = study;
    }

    public static StudyAnnouncement create(String title, String link, Study study) {
        return StudyAnnouncement.builder().title(title).link(link).study(study).build();
    }

    public void update(String title, String link) {
        this.title = title;
        this.link = link;
    }
}
