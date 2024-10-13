package com.gdschongik.gdsc.domain.study.dto.request;

import com.gdschongik.gdsc.domain.study.domain.AchievementType;
import java.util.List;

public record OutstandingStudentRequest(List<Long> studentIds, AchievementType achievementType) {}
