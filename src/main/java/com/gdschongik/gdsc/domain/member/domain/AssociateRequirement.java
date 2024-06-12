package com.gdschongik.gdsc.domain.member.domain;

import static com.gdschongik.gdsc.domain.common.model.RequirementStatus.*;

import com.gdschongik.gdsc.domain.common.model.RequirementStatus;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssociateRequirement {

    @Enumerated(EnumType.STRING)
    private RequirementStatus univStatus;

    @Enumerated(EnumType.STRING)
    private RequirementStatus discordStatus;

    @Enumerated(EnumType.STRING)
    private RequirementStatus bevyStatus;

    @Enumerated(EnumType.STRING)
    private RequirementStatus infoStatus;

    @Builder(access = AccessLevel.PRIVATE)
    private AssociateRequirement(
            RequirementStatus univStatus,
            RequirementStatus discordStatus,
            RequirementStatus bevyStatus,
            RequirementStatus infoStatus) {
        this.univStatus = univStatus;
        this.discordStatus = discordStatus;
        this.bevyStatus = bevyStatus;
        this.infoStatus = infoStatus;
    }

    public static AssociateRequirement createRequirement() {
        return AssociateRequirement.builder()
                .univStatus(PENDING)
                .discordStatus(PENDING)
                .bevyStatus(PENDING)
                .infoStatus(PENDING)
                .build();
    }

    // 상태 변경 로직

    public void verifyUniv() {
        this.univStatus = VERIFIED;
    }

    public void verifyDiscord() {
        this.discordStatus = VERIFIED;
    }

    public void verifyBevy() {
        this.bevyStatus = VERIFIED;
    }

    public void verifyInfo() {
        this.infoStatus = VERIFIED;
    }

    // 데이터 전달 로직

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
        return isUnivVerified() && isDiscordVerified() && isBevyVerified() && isInfoVerified();
    }
}
