package com.gdschongik.gdsc.domain.member.domain;

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

@Entity
@Getter
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

    private String department;

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
            String department,
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

    public void signup(String studentId, String name, String phone, String department, String email) {
        validateStatusUpdatable();
        validateUnivStatus();

        this.studentId = studentId;
        this.name = name;
        this.phone = phone;
        this.department = department;
        this.email = email;
    }

    public void withdraw() {
        if (isDeleted()) {
            throw new CustomException(MEMBER_DELETED);
        }
        this.status = MemberStatus.DELETED;
    }

    private boolean isDeleted() {
        return this.status.isDeleted();
    }

    private boolean isForbidden() {
        return this.status.isForbidden();
    }

    public void updateMemberInfo(
            String studentId,
            String name,
            String phone,
            String department,
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

    private void validateStatusUpdatable() {
        if (isDeleted()) {
            throw new CustomException(MEMBER_DELETED);
        }
        if (isForbidden()) {
            throw new CustomException(MEMBER_FORBIDDEN);
        }
    }

    private void validateUnivStatus() {
        if (this.requirement.isUnivPending()) {
            throw new CustomException(UNIV_NOT_VERIFIED);
        }
    }

    public void grant() {
        this.role = MemberRole.USER;
    }
}
