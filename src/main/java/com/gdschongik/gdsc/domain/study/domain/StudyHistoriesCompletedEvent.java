package com.gdschongik.gdsc.domain.study.domain;

import java.util.List;
import lombok.NonNull;

public record StudyHistoriesCompletedEvent(@NonNull List<Long> studyHistoryIds) {}
