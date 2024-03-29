package com.gdschongik.gdsc.domain.member.domain;

import static com.gdschongik.gdsc.domain.member.domain.MemberRole.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.BaseTimeEntity;
import com.gdschongik.gdsc.global.exception.CustomException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@SQLRestriction("status='NORMAL'")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    private String name;

    private String studentId;

    @Enumerated(EnumType.STRING)
    private Department department;

    private String email;

    private String phone;

    private String discordUsername;

    private String nickname;

    @Column(nullable = false)
    private String oauthId;

    private LocalDateTime lastLoginAt;

    private String univEmail;

    @Embedded
    private Requirement requirement;

    @Builder(access = AccessLevel.PRIVATE)
    private Member(
            MemberRole role,
            MemberStatus status,
            String name,
            String studentId,
            Department department,
            String email,
            String phone,
            String discordUsername,
            String nickname,
            String oauthId,
            LocalDateTime lastLoginAt,
            String univEmail,
            Requirement requirement) {
        this.role = role;
        this.status = status;
        this.name = name;
        this.studentId = studentId;
        this.department = department;
        this.email = email;
        this.phone = phone;
        this.discordUsername = discordUsername;
        this.nickname = nickname;
        this.oauthId = oauthId;
        this.lastLoginAt = lastLoginAt;
        this.univEmail = univEmail;
        this.requirement = requirement;
    }

    public static Member createGuestMember(String oauthId) {
        Requirement requirement = Requirement.createRequirement();
        return Member.builder()
                .oauthId(oauthId)
                .role(MemberRole.GUEST)
                .status(MemberStatus.NORMAL)
                .requirement(requirement)
                .build();
    }

    // 회원 검증 로직

    /**
     * 회원 상태를 변경할 수 있는지 검증합니다. 삭제되거나 차단된 회원은 상태를 변경할 수 없습니다.<br>
     * 대부분의 상태 변경 로직에서 사용됩니다.
     */
    private void validateStatusUpdatable() {
        if (this.status.isDeleted()) {
            throw new CustomException(MEMBER_DELETED);
        }
        if (this.status.isForbidden()) {
            throw new CustomException(MEMBER_FORBIDDEN);
        }
    }

    /**
     * 재학생 인증 여부를 검증합니다.
     */
    private void validateUnivStatus() {
        if (this.requirement.isUnivVerified() && this.univEmail != null) {
            return;
        }

        throw new CustomException(UNIV_NOT_VERIFIED);
    }

    /**
     * 회원 승인 가능 여부를 검증합니다.
     */
    private void validateGrantAvailable() {
        if (isGranted()) {
            throw new CustomException(MEMBER_ALREADY_GRANTED);
        }

        if (!this.requirement.isPaymentVerified()) {
            throw new CustomException(PAYMENT_NOT_VERIFIED);
        }

        if (!this.requirement.isDiscordVerified() || this.discordUsername == null || this.nickname == null) {
            throw new CustomException(DISCORD_NOT_VERIFIED);
        }

        if (!this.requirement.isBevyVerified()) {
            throw new CustomException(BEVY_NOT_VERIFIED);
        }

        validateUnivStatus();
    }

    // 회원 가입상태 변경 로직

    /**
     * 가입 신청 시 작성한 정보를 저장합니다. 재학생 인증을 완료한 회원만 신청할 수 있습니다.
     */
    public void signup(String studentId, String name, String phone, Department department, String email) {
        validateStatusUpdatable();
        validateUnivStatus();

        this.studentId = studentId;
        this.name = name;
        this.phone = phone;
        this.department = department;
        this.email = email;
    }

    /**
     * 가입 신청을 승인합니다.<br>
     * 어드민만 사용할 수 있어야 합니다.
     */
    public void grant() {
        validateStatusUpdatable();
        validateGrantAvailable();

        this.role = USER;
    }

    /**
     * 해당 회원을 탈퇴 처리합니다.
     */
    public void withdraw() {
        if (this.status.isDeleted()) {
            throw new CustomException(MEMBER_DELETED);
        }
        this.status = MemberStatus.DELETED;
    }

    /**
     * 회원 정보를 수정합니다. 어드민만 사용할 수 있어야 합니다.
     */
    public void updateMemberInfo(
            String studentId,
            String name,
            String phone,
            Department department,
            String email,
            String discordUsername,
            String nickname) {
        validateStatusUpdatable();

        this.studentId = studentId;
        this.name = name;
        this.phone = phone;
        this.department = department;
        this.email = email;
        this.discordUsername = discordUsername;
        this.nickname = nickname;
    }

    public void completeUnivEmailVerification(String univEmail) {
        this.univEmail = univEmail;
        requirement.updateUnivStatus(RequirementStatus.VERIFIED);
    }

    // 가입조건 인증 로직
    public void verifyDiscord(String discordUsername, String nickname) {
        validateStatusUpdatable();

        this.requirement.verifyDiscord();
        this.discordUsername = discordUsername;
        this.nickname = nickname;
    }

    public void updatePaymentStatus(RequirementStatus status) {
        validateStatusUpdatable();
        this.requirement.updatePaymentStatus(status);
    }

    public void verifyBevy() {
        validateStatusUpdatable();
        this.requirement.verifyBevy();
    }

    // 데이터 전달 로직

    public boolean isGranted() {
        return role.equals(USER) || role.equals(MemberRole.ADMIN);
    }

    /**
     * 회원 승인 가능 여부를 반환합니다.
     *
     * @see com.gdschongik.gdsc.domain.member.dao.MemberQueryMethod#isGrantAvailable()
     */
    public boolean isGrantAvailable() {
        try {
            validateGrantAvailable();
            return true;
        } catch (CustomException e) {
            return false;
        }
    }

    /**
     * 가입 신청서 제출 여부를 반환합니다.
     */
    public boolean isApplied() {
        return studentId != null;
    }

    // 기타 로직

    public void updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }
}
