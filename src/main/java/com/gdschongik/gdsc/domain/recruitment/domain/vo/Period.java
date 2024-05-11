package com.gdschongik.gdsc.domain.recruitment.domain.vo;

import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Embeddable
public class Period {
    private LocalDateTime startDate;

    private LocalDateTime endDate;
    @Builder(access = AccessLevel.PRIVATE)
    private Period(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Period createPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return Period.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
