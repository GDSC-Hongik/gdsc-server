package com.gdschongik.gdsc.domain.member.dto.request;

public record MemberQueryRequest(
        String studentId,
        String name,
        String phone,
        String department,
        String email,
        String discordUsername,
        String discordNickname) {}
