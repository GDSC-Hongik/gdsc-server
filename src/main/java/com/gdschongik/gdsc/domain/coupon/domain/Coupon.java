package com.gdschongik.gdsc.domain.coupon.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.BaseEntity;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.global.exception.CustomException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    private String name;

    @Embedded
    private Money discountAmount;

    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @Enumerated(EnumType.STRING)
    private IssuanceMethodType issuanceMethodType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @Builder(access = AccessLevel.PRIVATE)
    private Coupon(
            String name,
            Money discountAmount,
            CouponType couponType,
            IssuanceMethodType issuanceMethodType,
            Study study) {
        this.name = name;
        this.discountAmount = discountAmount;
        this.couponType = couponType;
        this.issuanceMethodType = issuanceMethodType;
        this.study = study;
    }

    // todo: 파라미터 수정 필요. api 수정시 같이 처리
    public static Coupon create(String name, Money discountAmount) {
        validateDiscountAmountPositive(discountAmount);
        return Coupon.builder().name(name).discountAmount(discountAmount).build();
    }

    // 검증 로직

    private static void validateDiscountAmountPositive(Money discountAmount) {
        if (!discountAmount.isGreaterThan(Money.ZERO)) {
            throw new CustomException(COUPON_DISCOUNT_AMOUNT_NOT_POSITIVE);
        }
    }
}
