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

    @Comment("회차 순서")
    private Integer position;

    @Comment("회차 제목")
    private String title;

    @Comment("회차 설명")
    private String description;

    // 수업 관련 필드

    @Comment("수업 출석 번호")
    private String lessonAttendanceNumber;

    @Embedded
    @AttributeOverride(name = "startDate", column = @Column(name = "lesson_start_at"))
    @AttributeOverride(name = "endDate", column = @Column(name = "lesson_end_at"))
    private Period lessonPeriod;

    // 과제 관련 필드

    @Comment("과제 명세 링크")
    private String assignmentDescriptionLink;

    @Embedded
    @AttributeOverride(name = "startDate", column = @Column(name = "assignment_start_at"))
    @AttributeOverride(name = "endDate", column = @Column(name = "assignment_end_at"))
    private Period assignmentPeriod;

    // 참조 필드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_v2_id")
    private StudyV2 studyV2;

    /**
     * 모든 스터디회차는 인자로 전달되는 스터디 애그리거트 루트 엔티티에 종속됩니다.
     * 따라서 스터디회차 생성 팩토리 메서드 생성 시 자기 자신을 반환하지 않고,
     * 스터디 애그리거트 루트 엔티티의 스터디회차 컬렉션에 자기 자신을 추가하는 식으로 생성합니다.
     * 부모 클래스에서 위 로직을 수행하기 때문에, 생성 팩토리 메서드는 void 타입을 반환해야 합니다.
     */
    @Builder(access = AccessLevel.PRIVATE)
    private StudySessionV2(
            Integer position,
            String title,
            String description,
            String lessonAttendanceNumber,
            Period lessonPeriod,
            String assignmentDescriptionLink,
            Period assignmentPeriod,
            StudyV2 studyV2) {
        this.position = position;
        this.title = title;
        this.description = description;
        this.lessonAttendanceNumber = lessonAttendanceNumber;
        this.lessonPeriod = lessonPeriod;
        this.assignmentDescriptionLink = assignmentDescriptionLink;
        this.assignmentPeriod = assignmentPeriod;
        this.studyV2 = studyV2;
        studyV2.getStudySessions().add(this);
    }

    public static void createEmptyForLive(Integer position, String lessonAttendanceNumber, StudyV2 studyV2) {
        StudySessionV2.builder()
                .position(position)
                .lessonAttendanceNumber(lessonAttendanceNumber)
                .studyV2(studyV2)
                .build();
    }

    public static void createEmptyForAssignment(Integer position, StudyV2 studyV2) {
        StudySessionV2.builder().position(position).studyV2(studyV2).build();
    }
}
