package com.gdschongik.gdsc.domain.studyv2.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.studyv2.dao.StudyV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudyStudentDto;
import com.gdschongik.gdsc.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonStudyServiceV2 {

    private final StudyV2Repository studyV2Repository;

    public StudyStudentDto getStudyInformation(Long studyId) {
        StudyV2 study =
                studyV2Repository.findFetchById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        return StudyStudentDto.from(study);
    }
}
