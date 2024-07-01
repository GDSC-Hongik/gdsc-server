package com.gdschongik.gdsc.domain.order.domain;

import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoneyInfo {

    @Comment("주문총액")
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "total_amount"))
    private Money totalAmount;

    @Comment("쿠폰할인금액")
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "discount_amount"))
    private Money discountAmount;

    @Comment("최종결제금액")
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "final_payment_amount"))
    private Money finalPaymentAmount;

    @Builder(access = AccessLevel.PRIVATE)
    private MoneyInfo(Money totalAmount, Money discountAmount, Money finalPaymentAmount) {
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalPaymentAmount = finalPaymentAmount;
    }

    public static MoneyInfo of(Money totalAmount, Money discountAmount, Money finalPaymentAmount) {
        validateFinalPaymentAmount(totalAmount, discountAmount, finalPaymentAmount);

        return MoneyInfo.builder()
                .totalAmount(totalAmount)
                .discountAmount(discountAmount)
                .finalPaymentAmount(finalPaymentAmount)
                .build();
    }

    private static void validateFinalPaymentAmount(Money totalAmount, Money discountAmount, Money finalPaymentAmount) {
        Money expectedFinalPaymentAmount = totalAmount.subtract(discountAmount);
        if (!finalPaymentAmount.equals(expectedFinalPaymentAmount)) {
            throw new CustomException(ErrorCode.ORDER_FINAL_PAYMENT_AMOUNT_MISMATCH);
        }
    }
}
