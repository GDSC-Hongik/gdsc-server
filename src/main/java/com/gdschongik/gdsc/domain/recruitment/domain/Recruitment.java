package com.gdschongik.gdsc.domain.recruitment.domain;

import com.gdschongik.gdsc.domain.common.model.BaseSemesterEntity;
import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.common.vo.Period;
import jakarta.persistence.*;
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

    private String feeName;

    @Embedded
    private Money fee;

    @Embedded
    private Period semesterPeriod;

    @Builder(access = AccessLevel.PRIVATE)
    private Recruitment(
            String feeName, Money fee, final Period semesterPeriod, Integer academicYear, SemesterType semesterType) {
        super(academicYear, semesterType);
        this.feeName = feeName;
        this.fee = fee;
        this.semesterPeriod = semesterPeriod;
    }

    public static Recruitment create(
            String feeName, Money fee, Period semesterPeriod, Integer academicYear, SemesterType semesterType) {
        return Recruitment.builder()
                .feeName(feeName)
                .fee(fee)
                .semesterPeriod(semesterPeriod)
                .academicYear(academicYear)
                .semesterType(semesterType)
                .build();
    }
}
