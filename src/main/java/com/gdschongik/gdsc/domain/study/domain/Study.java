package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.STUDY_APPLICATION_START_DATE_INVALID;

import com.gdschongik.gdsc.domain.common.model.BaseSemesterEntity;
import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
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
import java.time.LocalDateTime;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member mentor;

    @Embedded
    private Period period;

    @Embedded
    @AttributeOverride(name = "startDate", column = @Column(name = "application_start_date"))
    @AttributeOverride(name = "endDate", column = @Column(name = "application_end_date"))
    private Period applicationPeriod;

    @Comment("총 주차수")
    private Long totalWeek;

    @Comment("스터디 상세 노션 링크(Text)")
    @Column(columnDefinition = "TEXT")
    private String notionLink;

    @Comment("스터디 한줄 소개")
    private String introduction;

    @Enumerated(EnumType.STRING)
    private StudyType studyType;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Builder(access = AccessLevel.PRIVATE)
    public Study(
            Integer academicYear,
            SemesterType semesterType,
            Member mentor,
            Period period,
            Period applicationPeriod,
            Long totalWeek,
            StudyType studyType,
            DayOfWeek dayOfWeek) {
        super(academicYear, semesterType);
        this.mentor = mentor;
        this.period = period;
        this.applicationPeriod = applicationPeriod;
        this.totalWeek = totalWeek;
        this.studyType = studyType;
        this.dayOfWeek = dayOfWeek;
    }

    public static Study createStudy(
            Integer academicYear,
            SemesterType semesterType,
            Member mentor,
            Period period,
            Period applicationPeriod,
            Long totalWeek,
            StudyType studyType,
            DayOfWeek dayOfWeek) {
        validateApplicationStartDateBeforeSessionStartDate(applicationPeriod.getStartDate(), period.getStartDate());
        return Study.builder()
                .academicYear(academicYear)
                .semesterType(semesterType)
                .mentor(mentor)
                .period(period)
                .applicationPeriod(applicationPeriod)
                .totalWeek(totalWeek)
                .studyType(studyType)
                .dayOfWeek(dayOfWeek)
                .build();
    }

    private static void validateApplicationStartDateBeforeSessionStartDate(
            LocalDateTime applicationStartDate, LocalDateTime startDate) {
        if (!applicationStartDate.isBefore(startDate)) {
            throw new CustomException(STUDY_APPLICATION_START_DATE_INVALID);
        }
    }
}
