package com.gdschongik.gdsc.domain.test.domain;

import com.gdschongik.gdsc.domain.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyMembership extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean paid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_member_id")
    private MyMember member;

    public MyMembership(MyMember member) {
        this.member = member;
        this.paid = false;
    }

    public void verifyPayment() {
        this.paid = true;
        registerEvent(new MembershipVerifiedEvent(this.id));
    }

    public record MembershipVerifiedEvent(Long membershipId) {}
}
