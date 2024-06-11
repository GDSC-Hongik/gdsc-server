package com.gdschongik.gdsc.domain.member.domain;

import static com.gdschongik.gdsc.domain.common.model.RequirementStatus.*;

import com.gdschongik.gdsc.domain.common.model.RequirementStatus;
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
public class AssociateRequirement {

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
    private AssociateRequirement(
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

    public static AssociateRequirement createRequirement() {
        return AssociateRequirement.builder()
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
        return this.isInfoVerified() && this.isDiscordVerified() && this.isBevyVerified() && this.isUnivVerified();
    }
}
