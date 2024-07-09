package com.gdschongik.gdsc.domain.study.domain.request;

import java.time.LocalDateTime;

public record AssignmentCreateRequest(String title, String descriptionNotionLink, LocalDateTime deadLine) {}
