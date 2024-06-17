package com.gdschongik.gdsc.domain.member.domain;

import static com.gdschongik.gdsc.domain.common.model.RequirementStatus.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.RequirementStatus;
import com.gdschongik.gdsc.global.exception.CustomException;
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

    public void verifyUniv() { this.univStatus = VERIFIED; }

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

    private boolean isUnivVerified() {
        return this.univStatus == VERIFIED;
    }

    private boolean isDiscordVerified() {
        return this.discordStatus == VERIFIED;
    }

    private boolean isBevyVerified() {
        return this.bevyStatus == VERIFIED;
    }

    private boolean isInfoVerified() {
        return this.infoStatus == VERIFIED;
    }

    // 검증 로직

    public void validateAllVerified() {
        if (!isUnivVerified()) {
            throw new CustomException(UNIV_NOT_VERIFIED);
        }

        if (!isDiscordVerified()) {
            throw new CustomException(DISCORD_NOT_VERIFIED);
        }

        if (!isBevyVerified()) {
            throw new CustomException(BEVY_NOT_VERIFIED);
        }

        if (!isInfoVerified()) {
            throw new CustomException(BASIC_INFO_NOT_VERIFIED);
        }
    }

    public void checkVerifiableUniv(){
        if (isUnivVerified()) {
            throw new CustomException(EMAIL_ALREADY_VERIFIED);
        }
    }
}
