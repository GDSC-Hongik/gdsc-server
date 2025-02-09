package com.gdschongik.gdsc.domain.studyv2.dto.response;

import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudyManagerDto;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudySessionManagerDto;
import java.util.List;

public record StudyManagerResponse(StudyManagerDto study, List<StudySessionManagerDto> studySessions) {}	
