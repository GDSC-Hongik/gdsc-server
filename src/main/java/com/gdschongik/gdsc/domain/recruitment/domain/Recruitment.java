package com.gdschongik.gdsc.domain.recruitment.domain;

import com.gdschongik.gdsc.domain.common.model.BaseEntity;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.common.vo.Semester;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruitment_id")
    private Long id;

    private String feeName;

    @Embedded
    private Money fee;

    @Embedded
    private Semester semester;

    @Embedded
    private Period semesterPeriod;

    @Builder(access = AccessLevel.PRIVATE)
    private Recruitment(String feeName, Money fee, final Period semesterPeriod, Semester semester) {
        this.feeName = feeName;
        this.fee = fee;
        this.semesterPeriod = semesterPeriod;
        this.semester = semester;
    }

    public static Recruitment create(String feeName, Money fee, Period semesterPeriod, Semester semester) {
        return Recruitment.builder()
                .feeName(feeName)
                .fee(fee)
                .semesterPeriod(semesterPeriod)
                .semester(semester)
                .build();
    }
}
