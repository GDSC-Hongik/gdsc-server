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

    @Embedded
    private Money fee;

    private String feeName;

    @Embedded
    private Period semesterPeriod;

    @Builder(access = AccessLevel.PRIVATE)
    private Recruitment(
            Integer academicYear, SemesterType semesterType, Money fee, String feeName, final Period semesterPeriod) {
        super(academicYear, semesterType);
        this.fee = fee;
        this.feeName = feeName;
        this.semesterPeriod = semesterPeriod;
    }

    public static Recruitment create(
            Integer academicYear, SemesterType semesterType, Money fee, String feeName, Period semesterPeriod) {
        return Recruitment.builder()
                .academicYear(academicYear)
                .semesterType(semesterType)
                .fee(fee)
                .feeName(feeName)
                .semesterPeriod(semesterPeriod)
                .build();
    }
}
