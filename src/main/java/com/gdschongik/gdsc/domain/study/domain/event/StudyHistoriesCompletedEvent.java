package com.gdschongik.gdsc.domain.study.domain.event;

import java.util.List;
import lombok.NonNull;

public record StudyHistoriesCompletedEvent(@NonNull List<Long> studyHistoryIds) {}
