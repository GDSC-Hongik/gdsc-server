package com.gdschongik.gdsc.domain.study.domain;

import com.gdschongik.gdsc.domain.common.model.BaseEntity;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.domain.study.domain.vo.Assignment;
import com.gdschongik.gdsc.domain.study.domain.vo.Session;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @Comment("현 회차 값")
    private Long currentCount;

    private String attendanceNumber;

    @Embedded
    private Period period;

    @Embedded
    @AttributeOverride(name = "title", column = @Column(name = "session_title"))
    @AttributeOverride(name = "isCancelled", column = @Column(name = "session_is_cancelled"))
    @AttributeOverride(name = "difficulty", column = @Column(name = "session_difficulty"))
    @AttributeOverride(name = "startAt", column = @Column(name = "session_start_at"))
    @AttributeOverride(name = "description", column = @Column(name = "session_description"))
    private Session session;

    @Embedded
    @AttributeOverride(name = "title", column = @Column(name = "assignment_title"))
    @AttributeOverride(name = "isCancelled", column = @Column(name = "assignment_is_cancelled"))
    @AttributeOverride(name = "difficulty", column = @Column(name = "assignment_difficulty"))
    private Assignment assignment;
}
