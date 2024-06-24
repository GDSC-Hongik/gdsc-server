package com.gdschongik.gdsc.domain.recruitment.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.BaseSemesterEntity;
import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.global.exception.CustomException;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment extends BaseSemesterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruitment_id")
    private Long id;

    private String name;

    @Embedded
    private Period period;

    @Embedded
    private Money fee;

    @Enumerated(EnumType.STRING)
    private RoundType roundType;

    @Builder(access = AccessLevel.PRIVATE)
    private Recruitment(
            String name,
            final Period period,
            Integer academicYear,
            SemesterType semesterType,
            Money fee,
            RoundType roundType) {
        super(academicYear, semesterType);
        this.name = name;
        this.period = period;
        this.fee = fee;
        this.roundType = roundType;
    }

    public static Recruitment createRecruitment(
            String name,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer academicYear,
            SemesterType semesterType,
            RoundType roundType,
            Money fee) {
        Period period = Period.createPeriod(startDate, endDate);
        return Recruitment.builder()
                .name(name)
                .period(period)
                .academicYear(academicYear)
                .semesterType(semesterType)
                .roundType(roundType)
                .fee(fee)
                .build();
    }

    public boolean isOpen() {
        return period.isOpen();
    }

    public void validatePeriodOverlap(LocalDateTime startDate, LocalDateTime endDate) {
        period.validatePeriodOverlap(startDate, endDate);
    }

    public void updateRecruitment(
            String name,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer academicYear,
            SemesterType semesterType,
            RoundType roundType,
            Money fee) {
        validatePeriodNotStarted();

        this.name = name;
        this.period = Period.createPeriod(startDate, endDate);
        super.updateAcademicYear(academicYear);
        super.updateSemesterType(semesterType);
        this.roundType = roundType;
        this.fee = fee;
    }

    public void validatePeriodNotStarted() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(period.getStartDate())) {
            throw new CustomException(RECRUITMENT_STARTDATE_ALREADY_PASSED);
        }
    }
}
