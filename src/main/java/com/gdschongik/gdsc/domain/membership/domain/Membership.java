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
                .regularRequirement(RegularRequirement.createUnsatisfiedRequirement())
                .academicYear(recruitment.getAcademicYear())
                .semesterType(recruitment.getSemesterType())
                .build();
    }

    // 검증 로직

    // TODO validateRegularRequirement처럼 로직 변경
    // TODO: 어드민인 경우 리쿠르팅 지원 및 결제에 대한 정책 검토 필요. 현재는 불가능하도록 설정
    private static void validateMembershipApplicable(Member member) {
        if (member.getRole().equals(MemberRole.ASSOCIATE)) {
            return;
        }

        throw new CustomException(MEMBERSHIP_NOT_APPLICABLE);
    }

    public void validateRegularRequirement() {
        if (isRegularRequirementAllSatisfied()) {
            throw new CustomException(MEMBERSHIP_ALREADY_SATISFIED);
        }
    }

    // 상태 변경 로직

    public void verifyPaymentStatus() {
        validateRegularRequirement();

        regularRequirement.updatePaymentStatus(SATISFIED);
        regularRequirement.validateAllSatisfied();

        registerEvent(new MemberRegularEvent(member.getId(), member.getDiscordId()));
    }

    // 데이터 전달 로직

    public boolean isRegularRequirementAllSatisfied() {
        return regularRequirement.isAllSatisfied();
    }
}
