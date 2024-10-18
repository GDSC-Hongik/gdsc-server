package com.gdschongik.gdsc.domain.test.domain;

import com.gdschongik.gdsc.domain.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean confirmed;

    private Long membershipId;

    private Long memberId;

    public MyOrder(Long membershipId, Long memberId) {
        this.confirmed = false;
        this.membershipId = membershipId;
        this.memberId = memberId;
    }

    public void confirm() {
        this.confirmed = true;
        registerEvent(new OrderConfirmedEvent(this.id));
    }

    public record OrderConfirmedEvent(Long orderId) {}
}
