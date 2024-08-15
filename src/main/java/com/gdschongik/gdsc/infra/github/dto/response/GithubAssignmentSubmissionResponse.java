package com.gdschongik.gdsc.infra.github.dto.response;

import java.time.LocalDateTime;

public record GithubAssignmentSubmissionResponse(Integer size, LocalDateTime committedAt) {}
