package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.BaseEntity;
import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.study.domain.vo.Assignment;
import com.gdschongik.gdsc.domain.study.domain.vo.Curriculum;
import com.gdschongik.gdsc.global.exception.CustomException;
import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private Long week; // TODO: Integer로 변경

    private String attendanceNumber;

    @Embedded
    private Period period;

    @Embedded
    @AttributeOverride(name = "title", column = @Column(name = "curriculum_title"))
    @AttributeOverride(name = "difficulty", column = @Column(name = "curriculum_difficulty"))
    @AttributeOverride(name = "startAt", column = @Column(name = "curriculum_start_at"))
    @AttributeOverride(name = "description", column = @Column(name = "curriculum_description"))
    @AttributeOverride(name = "status", column = @Column(name = "curriculum_status"))
    private Curriculum curriculum;

    @Embedded
    @AttributeOverride(name = "title", column = @Column(name = "assignment_title"))
    @AttributeOverride(name = "deadline", column = @Column(name = "assignment_deadline"))
    @AttributeOverride(name = "descriptionLink", column = @Column(name = "assignment_description_link"))
    @AttributeOverride(name = "status", column = @Column(name = "assignment_status"))
    private Assignment assignment;

    @Builder(access = AccessLevel.PRIVATE)
    private StudyDetail(
            Study study,
            Long week,
            String attendanceNumber,
            Period period,
            Curriculum curriculum,
            Assignment assignment) {
        this.study = study;
        this.week = week;
        this.attendanceNumber = attendanceNumber;
        this.period = period;
        this.curriculum = curriculum;
        this.assignment = assignment;
    }

    public static StudyDetail create(Study study, Long week, String attendanceNumber, Period period) {
        return StudyDetail.builder()
                .study(study)
                .week(week)
                .period(period)
                .attendanceNumber(attendanceNumber)
                .period(period)
                .curriculum(Curriculum.empty())
                .assignment(Assignment.empty())
                .build();
    }

    public void cancelAssignment() {
        assignment = Assignment.canceled();
    }

    public void publishAssignment(String title, LocalDateTime deadLine, String descriptionNotionLink) {
        assignment = Assignment.of(title, deadLine, descriptionNotionLink);
    }

    public void updateAssignment(String title, LocalDateTime deadLine, String descriptionNotionLink) {
        assignment = Assignment.of(title, deadLine, descriptionNotionLink);
    }

    // 데이터 전달 로직

    public boolean isAssignmentDeadlineRemaining(LocalDateTime now) {
        return assignment.isDeadlineRemaining(now);
    }

    public boolean isAssignmentDeadlineThisWeek(LocalDate now) {
        return assignment.isDeadLineThisWeek(now);
    }

    // 스터디 시작일자 + 현재 주차 * 7 + (스터디 요일 - 스터디 기간 시작 요일)
    public LocalDate getAttendanceDay() {
        // 스터디 시작일자
        LocalDate startDate = study.getStartDate();

        // 스터디 요일
        DayOfWeek studyDayOfWeek = study.getDayOfWeek();

        // 스터디 기간 시작 요일
        DayOfWeek startDayOfWeek = startDate.getDayOfWeek();

        // 스터디 요일이 스터디 기간 시작 요일보다 앞서면, 다음 주로 넘어가게 처리
        Long daysDifference = Long.valueOf(studyDayOfWeek.getValue() - startDayOfWeek.getValue());
        if (daysDifference < 0) {
            daysDifference += 7;
        }

        // 현재 주차에 따른 일수 계산
        Long daysToAdd = (week - 1) * 7 + daysDifference;

        return startDate.plusDays(daysToAdd);
    }

    // 출석일이 오늘 or 오늘이후인지 확인
    public boolean isAttendanceDayNotPassed(LocalDate now) {
        return !getAttendanceDay().isBefore(now);
    }

    public void updateCurriculum(
            LocalTime startAt, String title, String description, Difficulty difficulty, StudyStatus status) {
        curriculum = Curriculum.of(startAt, title, description, difficulty, status);
    }

    public void validateAssignmentSubmittable(LocalDateTime now) {
        if (now.isBefore(period.getStartDate())) {
            throw new CustomException(ASSIGNMENT_SUBMIT_NOT_STARTED);
        }
        assignment.validateSubmittable(now);
    }
}
