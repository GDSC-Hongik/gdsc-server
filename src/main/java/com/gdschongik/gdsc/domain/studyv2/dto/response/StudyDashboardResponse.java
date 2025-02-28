package com.gdschongik.gdsc.domain.studyv2.dto.response;

import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudySessionMyDto;
import java.util.List;

public record StudyDashboardResponse(Long studyId, List<StudySessionMyDto> studySessions) {}
