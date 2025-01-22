package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.domain.study.domain.StudyType.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.BaseSemesterEntity;
import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import jakarta.persistence.AttributeOverride;
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
public class Study extends BaseSemesterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long id;

    private String title;

    @Comment("총 주차수")
    private Long totalWeek;

    @Comment("스터디 상세 노션 링크(Text)")
    @Column(columnDefinition = "TEXT")
    private String notionLink;

    @Comment("스터디 한줄 소개")
    private String introduction;

    @Comment("스터디 시작 시간")
    private LocalTime startTime;

    @Comment("스터디 종료 시간")
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private StudyType studyType;

    @Comment("스터디 요일")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Embedded
    private Period period;

    @Embedded
    @AttributeOverride(name = "startDate", column = @Column(name = "application_start_date"))
    @AttributeOverride(name = "endDate", column = @Column(name = "application_end_date"))
    private Period applicationPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member mentor;

    @Builder(access = AccessLevel.PRIVATE)
    private Study(
            String title,
            Long totalWeek,
            LocalTime startTime,
            LocalTime endTime,
            StudyType studyType,
            DayOfWeek dayOfWeek,
            Period period,
            Period applicationPeriod,
            Member mentor,
            Integer academicYear,
            SemesterType semesterType) {
        super(academicYear, semesterType);
        this.title = title;
        this.totalWeek = totalWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.studyType = studyType;
        this.dayOfWeek = dayOfWeek;
        this.period = period;
        this.applicationPeriod = applicationPeriod;
        this.mentor = mentor;
    }

    public static Study create(
            String title,
            Long totalWeek,
            LocalTime startTime,
            LocalTime endTime,
            StudyType studyType,
            DayOfWeek dayOfWeek,
            Period period,
            Period applicationPeriod,
            Member mentor,
            Integer academicYear,
            SemesterType semesterType) {
        validateApplicationStartDateBeforeCurriculumStartDate(applicationPeriod.getStartDate(), period.getStartDate());
        validateMentorRole(mentor);
        validateStudyTime(studyType, startTime, endTime);
        return Study.builder()
                .title(title)
                .totalWeek(totalWeek)
                .startTime(startTime)
                .endTime(endTime)
                .studyType(studyType)
                .dayOfWeek(dayOfWeek)
                .period(period)
                .applicationPeriod(applicationPeriod)
                .mentor(mentor)
                .academicYear(academicYear)
                .semesterType(semesterType)
                .build();
    }

    // 검증 로직

    private static void validateApplicationStartDateBeforeCurriculumStartDate(
            LocalDateTime applicationStartDate, LocalDateTime startDate) {
        if (!applicationStartDate.isBefore(startDate)) {
            throw new CustomException(STUDY_APPLICATION_START_DATE_INVALID);
        }
    }

    private static void validateMentorRole(Member mentor) {
        if (mentor.isGuest()) {
            throw new CustomException(STUDY_MENTOR_IS_UNAUTHORIZED);
        }
    }

    private static void validateStudyTime(StudyType studyType, LocalTime studyStartTime, LocalTime studyEndTime) {
        if (studyType == OFFLINE || studyType == ONLINE) {
            validateOnOffLineStudyTime(studyStartTime, studyEndTime);
        }
        if (studyType == ASSIGNMENT) {
            validateAssignmentLineStudyTime(studyStartTime, studyEndTime);
        }
    }

    private static void validateOnOffLineStudyTime(LocalTime studyStartTime, LocalTime studyEndTime) {
        if (!(studyStartTime != null && studyEndTime != null)) {
            throw new CustomException(ON_OFF_LINE_STUDY_TIME_IS_ESSENTIAL);
        } else if (!studyStartTime.isBefore(studyEndTime)) {
            throw new CustomException(STUDY_TIME_INVALID);
        }
    }

    private static void validateAssignmentLineStudyTime(LocalTime studyStartTime, LocalTime studyEndTime) {
        if (!(studyStartTime == null && studyEndTime == null)) {
            throw new CustomException(ASSIGNMENT_STUDY_CAN_NOT_INPUT_STUDY_TIME);
        }
    }

    // 데이터 전달 로직
    public boolean isApplicable() {
        return applicationPeriod.isOpen();
    }

    public boolean isWithinApplicationAndCourse(LocalDateTime now) {
        return applicationPeriod.getStartDate().isBefore(now)
                && period.getEndDate().isAfter(now);
    }

    public LocalDate getStartDate() {
        return period.getStartDate().toLocalDate();
    }

    public void update(String link, String introduction) {
        notionLink = link;
        this.introduction = introduction;
    }
}
