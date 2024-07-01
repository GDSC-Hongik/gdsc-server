package com.gdschongik.gdsc.domain.study.domain;

import com.gdschongik.gdsc.domain.common.model.BaseSemesterEntity;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study extends BaseSemesterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member mentor;

    @Embedded
    private Period period;

    // 총 주차수
    private Long totalWeek;

    // 스터디 상세 노션 링크(Text)
    @Column(columnDefinition = "TEXT")
    private String notionLink;

    // 스터디 한줄 소개
    private String introduction;

    @Enumerated(EnumType.STRING)
    private StudyType studyType;
}
