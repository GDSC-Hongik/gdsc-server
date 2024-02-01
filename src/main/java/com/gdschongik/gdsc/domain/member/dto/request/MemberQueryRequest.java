package com.gdschongik.gdsc.domain.member.dto.request;

public record MemberQueryRequest(
        String studentId,
        String name,
        String phone,
        String department,
        String email,
        String discordUsername,
        String discordNickname) {

    public static MemberQueryRequest of(
            String studentId,
            String name,
            String phone,
            String department,
            String email,
            String discordUsername,
            String discordNickname) {
        return new MemberQueryRequest(studentId, name, phone, department, email, discordUsername, discordNickname);
    }
}
