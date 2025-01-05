package com.gdschongik.gdsc.domain.study.dto.request;

import java.util.List;

public record StudyCompleteRequest(Long studyId, List<Long> studentIds) {}
