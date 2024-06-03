package com.gdschongik.gdsc.domain.coupon.domain;

import com.gdschongik.gdsc.domain.common.model.BaseTimeEntity;
import com.gdschongik.gdsc.domain.common.vo.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    private String name;

    @Embedded
    private Money discountAmount;

    @Builder(access = AccessLevel.PRIVATE)
    public Coupon(String name, Money discountAmount) {
        this.name = name;
        this.discountAmount = discountAmount;
    }

    public static Coupon createCoupon(String name, Money discountAmount) {
        return Coupon.builder().name(name).discountAmount(discountAmount).build();
    }
}
