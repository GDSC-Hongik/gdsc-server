package com.gdschongik.gdsc.domain.member.domain;

import static com.gdschongik.gdsc.domain.member.domain.RequirementStatus.*;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Requirement {

    @Enumerated(EnumType.STRING)
    private RequirementStatus univStatus;

    @Enumerated(EnumType.STRING)
    private RequirementStatus discordStatus;

    @Enumerated(EnumType.STRING)
    private RequirementStatus paymentStatus;

    @Builder(access = AccessLevel.PRIVATE)
    private Requirement(
            RequirementStatus univStatus, RequirementStatus discordStatus, RequirementStatus paymentStatus) {
        this.univStatus = univStatus;
        this.discordStatus = discordStatus;
        this.paymentStatus = paymentStatus;
    }

    public static Requirement createRequirement() {
        return Requirement.builder()
                .univStatus(PENDING)
                .discordStatus(PENDING)
                .paymentStatus(PENDING)
                .build();
    }

    public boolean isUnivPending() {
        return this.univStatus == PENDING;
    }
}
