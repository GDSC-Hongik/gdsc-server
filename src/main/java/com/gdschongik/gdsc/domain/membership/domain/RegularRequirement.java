package com.gdschongik.gdsc.domain.membership.domain;

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
public class RegularRequirement {

    @Enumerated(EnumType.STRING)
    private RequirementStatus paymentStatus;

    @Builder(access = AccessLevel.PRIVATE)
    private RegularRequirement(RequirementStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public static RegularRequirement createUnverifiedRequirement() {
        return RegularRequirement.builder()
                .paymentStatus(RequirementStatus.PENDING)
                .build();
    }

    public void updatePaymentStatus(RequirementStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isPaymentVerified() {
        return this.paymentStatus == RequirementStatus.VERIFIED;
    }

    public boolean isAllVerified() {
        return isPaymentVerified();
    }
}
