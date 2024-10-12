package com.gdschongik.gdsc.domain.membership.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.PAYMENT_NOT_SATISFIED;

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
public class RegularRequirement {

    @Enumerated(EnumType.STRING)
    private RequirementStatus paymentStatus;

    @Builder(access = AccessLevel.PRIVATE)
    private RegularRequirement(RequirementStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public static RegularRequirement create() {
        return RegularRequirement.builder()
                .paymentStatus(RequirementStatus.UNSATISFIED)
                .build();
    }

    public void updatePaymentStatus(RequirementStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isPaymentSatisfied() {
        return paymentStatus == RequirementStatus.SATISFIED;
    }

    /**
     * 정회원 승급 조건은 추가될 가능성이 존재
     */
    public boolean isAllSatisfied() {
        return isPaymentSatisfied();
    }

    public void validateAllSatisfied() {
        if (!isPaymentSatisfied()) {
            throw new CustomException(PAYMENT_NOT_SATISFIED);
        }
    }
}
