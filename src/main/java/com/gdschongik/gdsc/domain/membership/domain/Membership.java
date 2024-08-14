package com.gdschongik.gdsc.domain.membership.domain;

import static com.gdschongik.gdsc.domain.common.model.RequirementStatus.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.BaseEntity;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.global.exception.CustomException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
public class Membership extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_round_id")
    private RecruitmentRound recruitmentRound;

    @Embedded
    private RegularRequirement regularRequirement;

    @Builder(access = AccessLevel.PRIVATE)
    private Membership(Member member, RecruitmentRound recruitmentRound, RegularRequirement regularRequirement) {
        this.member = member;
        this.recruitmentRound = recruitmentRound;
        this.regularRequirement = regularRequirement;
    }

    public static Membership createMembership(Member member, RecruitmentRound recruitmentRound) {
        return Membership.builder()
                .member(member)
                .recruitmentRound(recruitmentRound)
                .regularRequirement(RegularRequirement.createUnsatisfiedRequirement())
                .build();
    }

    // 검증 로직

    public void validateRegularRequirement() {
        if (isRegularRequirementAllSatisfied()) {
            throw new CustomException(MEMBERSHIP_ALREADY_SATISFIED);
        }
    }

    // 상태 변경 로직

    public void verifyPaymentStatus() {
        validateRegularRequirement();

        regularRequirement.updatePaymentStatus(SATISFIED);

        registerEvent(new MembershipVerifiedEvent(id));
    }

    public void revokePaymentStatus() {
        validatePaymentStatusRevocable();

        regularRequirement.updatePaymentStatus(UNSATISFIED);

        registerEvent(new MembershipPaymentRevokedEvent(id));
    }

    private void validatePaymentStatusRevocable() {
        // TODO: 이벤트로 트리거되는 로직이더라도 예외 던지도록 수정
        if (!regularRequirement.isPaymentSatisfied()) {
            throw new CustomException(MEMBERSHIP_PAYMENT_NOT_REVOCABLE_NOT_SATISFIED);
        }
    }

    // 데이터 전달 로직

    public boolean isRegularRequirementAllSatisfied() {
        return regularRequirement.isAllSatisfied();
    }
}
