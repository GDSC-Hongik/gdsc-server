package com.gdschongik.gdsc.domain.membership.domain;

import static com.gdschongik.gdsc.domain.common.model.RequirementStatus.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.BaseSemesterEntity;
import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRegularEvent;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
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

    @Embedded
    private RegularRequirement regularRequirement;

    @Builder(access = AccessLevel.PRIVATE)
    private Membership(
            Member member,
            Recruitment recruitment,
            RegularRequirement regularRequirement,
            Integer academicYear,
            SemesterType semesterType) {
        super(academicYear, semesterType);
        this.member = member;
        this.recruitment = recruitment;
        this.regularRequirement = regularRequirement;
    }

    public static Membership createMembership(Member member, Recruitment recruitment) {
        validateMembershipApplicable(member);

        return Membership.builder()
                .member(member)
                .recruitment(recruitment)
                .regularRequirement(RegularRequirement.createUnverifiedRequirement())
                .academicYear(recruitment.getAcademicYear())
                .semesterType(recruitment.getSemesterType())
                .build();
    }

    // 검증 로직

    // TODO validateAdvanceRequirement처럼 로직 변경
    private static void validateMembershipApplicable(Member member) {
        if (member.getRole().equals(MemberRole.ASSOCIATE)) {
            return;
        }

        throw new CustomException(MEMBERSHIP_NOT_APPLICABLE);
    }

    // 상태 변경 로직

    public void verifyPaymentStatus() {
        this.regularRequirement.updatePaymentStatus(VERIFIED);

        registerEvent(new MemberRegularEvent(member.getId(), member.getDiscordUsername()));
    }

    // 데이터 전달 로직

    public boolean isRegularRequirementAllSatisfied() {
        return this.regularRequirement.isAllVerified();
    }

    public void validateAdvanceRequirement() {
        if (isRegularRequirementAllSatisfied()) {
            throw new CustomException(MEMBERSHIP_ALREADY_VERIFIED);
        }
    }
}
