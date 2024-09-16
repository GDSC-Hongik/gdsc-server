package com.gdschongik.gdsc.domain.recruitment.dto;

import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.domain.recruitment.domain.RoundType;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import java.math.BigDecimal;

public record RecruitmentRoundFullDto(
        Long recruitmentId,
        String name,
        Period period,
        String feeName,
        BigDecimal fee,
        RoundType roundType,
        String roundTypeValue) {
    public static RecruitmentRoundFullDto from(RecruitmentRound recruitmentRound) {
        return new RecruitmentRoundFullDto(
                recruitmentRound.getId(),
                recruitmentRound.getName(),
                recruitmentRound.getPeriod(),
                recruitmentRound.getRecruitment().getFeeName(),
                recruitmentRound.getRecruitment().getFee().getAmount(),
                recruitmentRound.getRoundType(),
                recruitmentRound.getRoundType().getValue());
    }
}
