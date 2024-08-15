package com.gdschongik.gdsc.infra.github.dto.response;

import java.time.LocalDateTime;

public record GithubAssignmentSubmissionResponse(String commitHash, Integer size, LocalDateTime committedAt) {}
