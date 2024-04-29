package com.gdschongik.gdsc.domain.application.domain;

import com.gdschongik.gdsc.domain.common.model.BaseTermEntity;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import jakarta.persistence.Column;
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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application extends BaseTermEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private RequirementStatus paymentStatus;

    @Builder(access = AccessLevel.PRIVATE)
    private Application(Member member, RequirementStatus paymentStatus) {
        this.member = member;
        this.paymentStatus = paymentStatus;
    }

    public static Application createApplication(Member member) {
        return Application.builder()
                .member(member)
                .paymentStatus(RequirementStatus.PENDING)
                .build();
    }
}
