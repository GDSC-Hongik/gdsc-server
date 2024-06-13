package com.gdschongik.gdsc.domain.recruitment.domain;

import com.gdschongik.gdsc.domain.common.model.BaseSemesterEntity;
import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
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

    private Money fee;

    @Enumerated(EnumType.STRING)
    private Round round;

    @Builder(access = AccessLevel.PRIVATE)
    private Recruitment(
            String name, final Period period, Integer academicYear, SemesterType semesterType, Money fee, Round round) {
        super(academicYear, semesterType);
        this.name = name;
        this.period = period;
        this.fee = fee;
        this.round = round;
    }

    public static Recruitment createRecruitment(
            String name,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer academicYear,
            SemesterType semesterType,
            Money fee,
            Round round) {
        Period period = Period.createPeriod(startDate, endDate);
        return Recruitment.builder()
                .name(name)
                .period(period)
                .academicYear(academicYear)
                .semesterType(semesterType)
                .fee(fee)
                .round(round)
                .build();
    }

    public boolean isOpen() {
        return this.period.isOpen();
    }

    public void validatePeriodOverlap(LocalDateTime startDate, LocalDateTime endDate) {
        this.period.validatePeriodOverlap(startDate, endDate);
    }
}
