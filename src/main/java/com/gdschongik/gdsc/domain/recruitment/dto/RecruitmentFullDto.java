package com.gdschongik.gdsc.domain.recruitment.dto;

import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.domain.recruitment.domain.RoundType;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import java.math.BigDecimal;

public record RecruitmentFullDto(
        Long recruitmentId, String name, Period period, BigDecimal fee, RoundType roundType, String roundTypeValue) {
    public static RecruitmentFullDto from(RecruitmentRound recruitmentRound) {
        return new RecruitmentFullDto(
                recruitmentRound.getId(),
                recruitmentRound.getName(),
                recruitmentRound.getPeriod(),
                recruitmentRound.getRecruitment().getFee().getAmount(),
                recruitmentRound.getRoundType(),
                recruitmentRound.getRoundType().getValue());
    }
}
