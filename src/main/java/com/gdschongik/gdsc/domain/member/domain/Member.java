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

    private String discordId;

    @Column(nullable = false)
    private String oauthId;

    private LocalDateTime lastLoginAt;

    private String univEmail;

    @Embedded
    private AssociateRequirement associateRequirement;

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
            AssociateRequirement associateRequirement) {
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
        this.associateRequirement = associateRequirement;
    }

    public static Member createGuestMember(String oauthId) {
        AssociateRequirement associateRequirement = AssociateRequirement.createRequirement();
        return Member.builder()
                .oauthId(oauthId)
                .role(GUEST)
                .status(MemberStatus.NORMAL)
                .associateRequirement(associateRequirement)
                .build();
    }

    // 상태 검증 로직

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
     * 준회원 승급 가능 여부를 검증합니다.
     */
    private void validateAssociateAvailable() {
        if (this.role.equals(ASSOCIATE)) {
            throw new CustomException(MEMBER_ALREADY_ASSOCIATE);
        }

        associateRequirement.validateAllVerified();
    }

    // 준회원 승급 관련 로직

    /**
     * 기본 회원 정보를 작성합니다.
     * 기본정보 인증상태를 인증 처리합니다.
     */
    public void updateBasicMemberInfo(
            String studentId, String name, String phone, Department department, String email) {
        validateStatusUpdatable();

        this.studentId = studentId;
        this.name = name;
        this.phone = phone;
        this.department = department;
        this.email = email;

        this.associateRequirement.verifyInfo();

        registerEvent(new MemberAssociateEvent(this.id));
    }

    /**
     * 재학생 이메일 인증을 진행합니다.
     * 재학생 이메일 인증상태를 인증 처리합니다.
     */
    public void completeUnivEmailVerification(String univEmail) {
        validateStatusUpdatable();

        this.univEmail = univEmail;

        associateRequirement.verifyUniv();

        registerEvent(new MemberAssociateEvent(this.id));
    }

    /**
     * 디스코드 서버와의 연동을 진행합니다.
     * 디스코드 인증상태를 인증 처리합니다.
     */
    public void verifyDiscord(String discordUsername, String nickname) {
        validateStatusUpdatable();
        this.associateRequirement.verifyDiscord();
        this.discordUsername = discordUsername;
        this.nickname = nickname;

        registerEvent(new MemberAssociateEvent(this.id));
    }

    /**
     * Bevy 서버와의 연동을 진행합니다.
     * Bevy 인증상태를 인증 처리합니다.
     */
    public void verifyBevy() {
        validateStatusUpdatable();
        this.associateRequirement.verifyBevy();

        registerEvent(new MemberAssociateEvent(this.id));
    }

    /**
     * GUEST -> 준회원으로 승급됩니다.
     * 모든 조건을 충족하면 서버에서 각각의 인증과정에서 자동으로 advanceToAssociate()호출된다
     * 조건 1 : 기본 회원정보 작성
     * 조건 2 : 재학생 인증
     * 조건 3 : 디스코드 인증
     * 조건 4 : Bevy 인증
     */
    public void advanceToAssociate() {
        validateStatusUpdatable();
        validateAssociateAvailable();

        this.role = ASSOCIATE;
    }

    // 기타 상태 변경 로직

    public void updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public void updateDiscordId(String discordId) {
        this.discordId = discordId;
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

    // 데이터 전달 로직

    public boolean isRegular() {
        return role.equals(REGULAR);
    }
}
