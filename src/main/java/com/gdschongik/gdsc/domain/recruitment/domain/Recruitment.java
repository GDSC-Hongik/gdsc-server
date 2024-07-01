package com.gdschongik.gdsc.domain.recruitment.domain;

import com.gdschongik.gdsc.domain.common.model.BaseSemesterEntity;
import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
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

    @Builder(access = AccessLevel.PRIVATE)
    private Recruitment(Integer academicYear, SemesterType semesterType, Money fee) {
        super(academicYear, semesterType);
        this.fee = fee;
    }

    public static Recruitment createRecruitment(Integer academicYear, SemesterType semesterType, Money fee) {
        return Recruitment.builder()
                .academicYear(academicYear)
                .semesterType(semesterType)
                .fee(fee)
                .build();
    }

    public void updateFee(Money fee) {
        this.fee = fee;
    }
}
