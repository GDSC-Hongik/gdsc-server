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

    @Enumerated(EnumType.STRING)
    private RequirementStatus bevyStatus;

    @Builder(access = AccessLevel.PRIVATE)
    private Requirement(
            RequirementStatus univStatus,
            RequirementStatus discordStatus,
            RequirementStatus paymentStatus,
            RequirementStatus bevyStatus) {
        this.univStatus = univStatus;
        this.discordStatus = discordStatus;
        this.paymentStatus = paymentStatus;
        this.bevyStatus = bevyStatus;
    }

    public static Requirement createRequirement() {
        return Requirement.builder()
                .univStatus(PENDING)
                .discordStatus(PENDING)
                .paymentStatus(PENDING)
                .bevyStatus(PENDING)
                .build();
    }

    public void updatePaymentStatus(RequirementStatus status) {
        this.paymentStatus = status;
    }

    public void verifyDiscord() {
        this.discordStatus = VERIFIED;
    }

    public void verifyBevy() {
        this.bevyStatus = VERIFIED;
    }

    public boolean isUnivVerified() {
        return this.univStatus == VERIFIED;
    }

    public boolean isDiscordVerified() {
        return this.discordStatus == VERIFIED;
    }

    public boolean isPaymentVerified() {
        return this.paymentStatus == VERIFIED;
    }

    public boolean isBevyVerified() {
        return this.bevyStatus == VERIFIED;
    }
}
