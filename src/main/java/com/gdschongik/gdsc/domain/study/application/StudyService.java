package com.gdschongik.gdsc.domain.study.application;

import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.dto.response.StudyResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyService {

    private final StudyRepository studyRepository;

    public List<StudyResponse> getAllApplicableStudies() {
        return studyRepository.findAll().stream()
                .filter(Study::isApplicable)
                .map(StudyResponse::from)
                .toList();
    }
}
