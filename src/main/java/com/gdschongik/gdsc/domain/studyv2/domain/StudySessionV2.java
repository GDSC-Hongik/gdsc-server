package com.gdschongik.gdsc.domain.studyv2.domain;

import com.gdschongik.gdsc.domain.common.model.BaseEntity;
import com.gdschongik.gdsc.domain.common.vo.Period;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "study_session_v2")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class StudySessionV2 extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_session_v2_id")
    private Long id;

    // 수업 관련 필드

    @Comment("수업 상태")
    private StudyStatusV2 lessonStatus;

    @Comment("수업 제목")
    private String lessonTitle;

    @Comment("수업 설명")
    private String lessonDescription;

    @Comment("수업 출석 번호")
    private Integer lessonAttendanceNumber;

    @Embedded
    @AttributeOverride(name = "startDate", column = @Column(name = "lesson_start_at"))
    @AttributeOverride(name = "endDate", column = @Column(name = "lesson_end_at"))
    private Period lessonPeriod;

    // 과제 관련 필드

    @Comment("과제 상태")
    private StudyStatusV2 assignmentStatus;

    @Comment("과제 제목")
    private String assignmentTitle;

    @Comment("과제 명세 링크")
    private String assignmentDescriptionLink;

    @Embedded
    @AttributeOverride(name = "startDate", column = @Column(name = "assignment_start_at"))
    @AttributeOverride(name = "endDate", column = @Column(name = "assignment_end_at"))
    private Period assignmentPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_v2_id")
    private StudyV2 studyV2;

    @Builder(access = AccessLevel.PRIVATE)
    private StudySessionV2(
            StudyStatusV2 lessonStatus,
            String lessonTitle,
            String lessonDescription,
            Integer lessonAttendanceNumber,
            Period lessonPeriod,
            StudyStatusV2 assignmentStatus,
            String assignmentTitle,
            String assignmentDescriptionLink,
            Period assignmentPeriod,
            StudyV2 studyV2) {
        this.lessonStatus = lessonStatus;
        this.lessonTitle = lessonTitle;
        this.lessonDescription = lessonDescription;
        this.lessonAttendanceNumber = lessonAttendanceNumber;
        this.lessonPeriod = lessonPeriod;
        this.assignmentStatus = assignmentStatus;
        this.assignmentTitle = assignmentTitle;
        this.assignmentDescriptionLink = assignmentDescriptionLink;
        this.assignmentPeriod = assignmentPeriod;
        this.studyV2 = studyV2;
    }

    public static StudySessionV2 create(
            StudyStatusV2 lessonStatus,
            String lessonTitle,
            String lessonDescription,
            Integer lessonAttendanceNumber,
            Period lessonPeriod,
            StudyStatusV2 assignmentStatus,
            String assignmentTitle,
            String assignmentDescriptionLink,
            Period assignmentPeriod,
            StudyV2 studyV2) {
        return StudySessionV2.builder()
                .lessonStatus(lessonStatus)
                .lessonTitle(lessonTitle)
                .lessonDescription(lessonDescription)
                .lessonAttendanceNumber(lessonAttendanceNumber)
                .lessonPeriod(lessonPeriod)
                .assignmentStatus(assignmentStatus)
                .assignmentTitle(assignmentTitle)
                .assignmentDescriptionLink(assignmentDescriptionLink)
                .assignmentPeriod(assignmentPeriod)
                .studyV2(studyV2)
                .build();
    }
}
