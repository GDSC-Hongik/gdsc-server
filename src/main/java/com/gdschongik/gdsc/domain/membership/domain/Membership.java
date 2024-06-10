package com.gdschongik.gdsc.domain.membership.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.BaseSemesterEntity;
import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @Enumerated(EnumType.STRING)
    private RequirementStatus paymentStatus;

    @Builder(access = AccessLevel.PRIVATE)
    private Membership(
            Member member,
            Recruitment recruitment,
            RequirementStatus paymentStatus,
            Integer academicYear,
            SemesterType semesterType) {
        super(academicYear, semesterType);
        this.member = member;
        this.paymentStatus = paymentStatus;
    }

    public static Membership createMembership(Member member, Recruitment recruitment) {
        validateMembershipApplicable(member);
        return Membership.builder()
                .member(member)
                .recruitment(recruitment)
                .paymentStatus(RequirementStatus.PENDING)
                .academicYear(recruitment.getAcademicYear())
                .semesterType(recruitment.getSemesterType())
                .build();
    }

    private static void validateMembershipApplicable(Member member) {
        if (member.getRole().equals(MemberRole.ASSOCIATE)) {
            return;
        }

        // todo: Member.grant() 작업 후 제거
        if (member.getRole().equals(MemberRole.USER)) {
            return;
        }

        throw new CustomException(MEMBERSHIP_NOT_APPLICABLE);
    }
}
