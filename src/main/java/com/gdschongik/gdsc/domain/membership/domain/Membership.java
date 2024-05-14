package com.gdschongik.gdsc.domain.membership.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.BaseSemesterEntity;
import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.global.exception.CustomException;
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
public class Membership extends BaseSemesterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private RequirementStatus paymentStatus;

    @Builder(access = AccessLevel.PRIVATE)
    private Membership(
            Member member, RequirementStatus paymentStatus, Integer academicYear, SemesterType semesterType) {
        super(academicYear, semesterType);
        this.member = member;
        this.paymentStatus = paymentStatus;
    }

    public static Membership createMembership(Member member, Integer academicYear, SemesterType semesterType) {
        validateMembershipApplicable(member);
        return Membership.builder()
                .member(member)
                .paymentStatus(RequirementStatus.PENDING)
                .academicYear(academicYear)
                .semesterType(semesterType)
                .build();
    }

    private static void validateMembershipApplicable(Member member) {
        if (member.getRole().equals(MemberRole.ASSOCIATE)) {
            return;
        }

        throw new CustomException(MEMBERSHIP_NOT_APPLICABLE);
    }
}
