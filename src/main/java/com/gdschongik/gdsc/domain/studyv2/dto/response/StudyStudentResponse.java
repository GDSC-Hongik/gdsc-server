package com.gdschongik.gdsc.domain.studyv2.dto.response;

import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudyCommonDto;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudySessionStudentDto;
import java.util.List;

public record StudyStudentResponse(StudyCommonDto study, List<StudySessionStudentDto> studySessions) {}
