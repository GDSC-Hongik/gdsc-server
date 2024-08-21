package com.gdschongik.gdsc.domain.study.domain;

import java.time.LocalDateTime;

public record AssignmentSubmission(String url, String commitHash, Integer contentLength, LocalDateTime committedAt) {}
