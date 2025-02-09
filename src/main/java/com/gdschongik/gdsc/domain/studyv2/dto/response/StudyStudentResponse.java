package com.gdschongik.gdsc.domain.studyv2.dto.response;

import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudySessionStudentDto;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudyStudentDto;
import java.util.List;

public record StudyStudentResponse(StudyStudentDto study, List<StudySessionStudentDto> studySessions) {}
