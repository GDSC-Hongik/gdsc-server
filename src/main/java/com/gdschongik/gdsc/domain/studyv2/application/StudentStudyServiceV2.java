package com.gdschongik.gdsc.domain.studyv2.application;

import org.springframework.stereotype.Service;

import com.gdschongik.gdsc.domain.studyv2.dto.response.StudentMyCurrentStudyResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentStudyServiceV2 {

    public StudentMyCurrentStudyResponse getMyCurrentStudies() {
        return new StudentMyCurrentStudyResponse();
    }
}
