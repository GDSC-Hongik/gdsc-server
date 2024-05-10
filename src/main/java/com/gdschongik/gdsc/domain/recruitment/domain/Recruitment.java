package com.gdschongik.gdsc.domain.recruitment.domain;

import com.gdschongik.gdsc.domain.common.model.BaseSemesterEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Builder(access = AccessLevel.PRIVATE)
    private Recruitment(String name, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Recruitment createRecruitment(String name, LocalDateTime startDate, LocalDateTime endDate) {
        return Recruitment.builder()
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
