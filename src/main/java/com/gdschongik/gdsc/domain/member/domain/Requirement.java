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

    @Enumerated(EnumType.STRING)
    private RequirementStatus infoStatus;

    @Builder(access = AccessLevel.PRIVATE)
    private Requirement(
            RequirementStatus univStatus,
            RequirementStatus discordStatus,
            RequirementStatus paymentStatus,
            RequirementStatus bevyStatus,
            RequirementStatus infoStatus) {
        this.univStatus = univStatus;
        this.discordStatus = discordStatus;
        this.paymentStatus = paymentStatus;
        this.bevyStatus = bevyStatus;
        this.infoStatus = infoStatus;
    }

    public static Requirement createRequirement() {
        return Requirement.builder()
                .univStatus(PENDING)
                .discordStatus(PENDING)
                .paymentStatus(PENDING)
                .bevyStatus(PENDING)
                .infoStatus(PENDING)
                .build();
    }

    public void updateUnivStatus(RequirementStatus univStatus) {
        this.univStatus = univStatus;
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

    public void verifyInfoStatus() {
        this.infoStatus = VERIFIED;
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

    public boolean isInfoVerified() {
        return this.infoStatus == VERIFIED;
    }

    public boolean isAllVerified() {
        return isAssociateAvailable();
    }

    private boolean isAssociateAvailable() {
        if (!this.isInfoVerified()) {
            return false;
        }

        if (!this.isDiscordVerified()) {
            return false;
        }

        if (!this.isBevyVerified()) {
            return false;
        }

        if (!this.isUnivVerified()) {
            return false;
        }
        return true;
    }
}
