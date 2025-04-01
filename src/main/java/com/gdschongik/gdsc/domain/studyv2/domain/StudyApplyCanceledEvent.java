package com.gdschongik.gdsc.domain.studyv2.domain;

public record StudyApplyCanceledEvent(String studyDiscordRoleId, String memberDiscordId, Long studyId, Long memberId) {}
