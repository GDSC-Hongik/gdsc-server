package com.gdschongik.gdsc.domain.recruitment.dto;

import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RoundType;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import java.math.BigDecimal;

public record RecruitmentFullDto(
        Long recruitmentId, String name, Period period, BigDecimal fee, RoundType roundType, String roundTypeValue) {
    public static RecruitmentFullDto from(Recruitment recruitment) {
        return new RecruitmentFullDto(
                recruitment.getId(),
                recruitment.getName(),
                recruitment.getPeriod(),
                recruitment.getFee().getAmount(),
                recruitment.getRoundType(),
                recruitment.getRoundType().getValue());
    }
}
