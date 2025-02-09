package com.gdschongik.gdsc.domain.studyv2.application;

import com.gdschongik.gdsc.domain.studyv2.dto.request.StudyUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorStudyServiceV2 {

    @Transactional
    public void updateStudy(Long studyId, StudyUpdateRequest request) {}
}
