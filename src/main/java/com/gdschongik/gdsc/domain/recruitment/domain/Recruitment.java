package com.gdschongik.gdsc.domain.recruitment.domain;

import com.gdschongik.gdsc.domain.common.model.BaseSemesterEntity;
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

    @Builder(access = AccessLevel.PRIVATE)
    private Recruitment(String name, final Period period) {
        this.name = name;
        this.period = period;
    }

    public static Recruitment createRecruitment(String name, LocalDateTime startDate, LocalDateTime endDate) {
        Period period = Period.createPeriod(startDate, endDate);
        return Recruitment.builder().name(name).period(period).build();
    }
}
