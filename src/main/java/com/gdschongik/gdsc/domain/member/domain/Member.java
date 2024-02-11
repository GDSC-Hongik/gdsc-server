package com.gdschongik.gdsc.domain.member.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.BaseTimeEntity;
import com.gdschongik.gdsc.domain.member.dto.request.MemberUpdateRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import jakarta.persistence.Column;
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
            String univEmail) {
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
    }

    public static Member createGuestMember(String oauthId) {
        return Member.builder()
                .oauthId(oauthId)
                .role(MemberRole.GUEST)
                .status(MemberStatus.NORMAL)
                .build();
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

    public void updateMemberInfo(MemberUpdateRequest request) {
        validateStatusUpdatable();

        this.studentId = request.studentId();
        this.name = request.name();
        this.phone = request.phone();
        this.department = request.department();
        this.email = request.email();
        this.discordUsername = request.discordUsername();
        this.nickname = request.nickname();
    }

    private void validateStatusUpdatable() {
        if (isDeleted()) {
            throw new CustomException(MEMBER_DELETED);
        }
        if (isForbidden()) {
            throw new CustomException(MEMBER_FORBIDDEN);
        }
    }
}
