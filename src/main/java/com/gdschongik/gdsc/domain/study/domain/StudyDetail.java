package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.BaseEntity;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.domain.study.domain.vo.Assignment;
import com.gdschongik.gdsc.domain.study.domain.vo.Session;
import com.gdschongik.gdsc.global.exception.CustomException;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Comment("현 주차수")
    private Long week;

    private String attendanceNumber;

    @Embedded
    private Period period;

    @Embedded
    @AttributeOverride(name = "title", column = @Column(name = "session_title"))
    @AttributeOverride(name = "difficulty", column = @Column(name = "session_difficulty"))
    @AttributeOverride(name = "startAt", column = @Column(name = "session_start_at"))
    @AttributeOverride(name = "description", column = @Column(name = "session_description"))
    @AttributeOverride(name = "status", column = @Column(name = "session_status"))
    private Session session;

    @Embedded
    @AttributeOverride(name = "title", column = @Column(name = "assignment_title"))
    @AttributeOverride(name = "difficulty", column = @Column(name = "assignment_difficulty"))
    @AttributeOverride(name = "status", column = @Column(name = "assignment_status"))
    private Assignment assignment;

    @Builder(access = AccessLevel.PRIVATE)
    private StudyDetail(
            Study study, Long week, String attendanceNumber, Period period, Session session, Assignment assignment) {
        this.study = study;
        this.week = week;
        this.attendanceNumber = attendanceNumber;
        this.period = period;
        this.session = session;
        this.assignment = assignment;
    }

    public static StudyDetail createStudyDetail(Study study, Long week, String attendanceNumber, Period period) {
        return StudyDetail.builder()
                .study(study)
                .week(week)
                .period(period)
                .attendanceNumber(attendanceNumber)
                .period(period)
                .session(Session.createEmptySession())
                .assignment(Assignment.createEmptyAssignment())
                .build();
    }

    public void cancelAssignment() {
        assignment = Assignment.cancelAssignment();
    }

    public void publishAssignment(String title, LocalDateTime deadLine, String descriptionNotionLink) {
        assignment = Assignment.generateAssignment(title, deadLine, descriptionNotionLink);
    }

    public void updateAssignment(String title, LocalDateTime deadLine, String descriptionNotionLink) {
        assignment = Assignment.generateAssignment(title, deadLine, descriptionNotionLink);
    }

    // 스터디 시작일자 + 현재 주차 * 7 + (스터디 요일 - 스터디 기간 시작 요일)
    public LocalDate getAttendanceDay() {
        return study.getStartDate()
                .plusDays(week * 7
                        + study.getDayOfWeek().getValue()
                        - study.getStartDate().getDayOfWeek().getValue());
    }

    public void validateAssignmentSubmittable(LocalDateTime now) {
        if (now.isBefore(period.getStartDate())) {
            throw new CustomException(ASSIGNMENT_SUBMIT_NOT_STARTED);
        }
        assignment.validateSubmittable(now);
    }
}
