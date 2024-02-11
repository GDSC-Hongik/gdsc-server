package com.gdschongik.gdsc.domain.requirement.domain;

import static com.gdschongik.gdsc.domain.requirement.domain.RequirementStatus.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Requirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requirement_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private RequirementStatus univStatus;

    @Enumerated(EnumType.STRING)
    private RequirementStatus discordStatus;

    @Enumerated(EnumType.STRING)
    private RequirementStatus paymentStatus;

    @Builder(access = AccessLevel.PRIVATE)
    private Requirement(
            RequirementStatus univStatus, RequirementStatus discordStatus, RequirementStatus paymentStatus) {
        this.univStatus = univStatus;
        this.discordStatus = discordStatus;
        this.paymentStatus = paymentStatus;
    }

    public static Requirement createInstance() {
        return Requirement.builder()
                .univStatus(PENDING)
                .discordStatus(PENDING)
                .paymentStatus(PENDING)
                .build();
    }
}
