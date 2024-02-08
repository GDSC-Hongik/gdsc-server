package com.gdschongik.gdsc.domain.member.domain;

import com.gdschongik.gdsc.domain.common.model.BaseTimeEntity;
import com.gdschongik.gdsc.domain.member.dto.request.MemberUpdateRequest;
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

    public void updateMemberInfo(MemberUpdateRequest request) {
        updateStudentId(request.studentId());
        updateName(request.name());
        updatePhone(request.phone());
        updateDepartment(request.department());
        updateEmail(request.email());
        updateDiscordUsername(request.discordUsername());
        updateNickname(request.discordNickname());
    }

    private void updateStudentId(String studentId) {
        if (studentId != null) {
            this.studentId = studentId;
        }
    }

    private void updateName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    private void updatePhone(String phone) {
        if (phone != null) {
            this.name = phone;
        }
    }

    private void updateDepartment(String department) {
        if (department != null) {
            this.department = department;
        }
    }


    private void updateEmail(String email) {
        if (email != null) {
            this.email = email;
        }
    }

    private void updateDiscordUsername(String discordUsername) {
        if (discordUsername != null) {
            this.discordUsername = discordUsername;
        }
    }

    private void updateNickname(String nickname) {
        if (nickname != null) {
            this.nickname = nickname;
        }
    }
}
