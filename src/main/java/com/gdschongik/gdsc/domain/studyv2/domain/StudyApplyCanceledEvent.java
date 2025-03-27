package com.gdschongik.gdsc.domain.studyv2.domain;

public record StudyApplyCanceledEvent(Long studyId, String studyDiscordRoleId, Long memberId, String memberDiscordId) {}
