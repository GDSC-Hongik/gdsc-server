package com.gdschongik.gdsc.domain.study.application;

import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.dto.response.AssignmentResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentStudyDetailService {

    private final StudyDetailRepository studyDetailRepository;

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getSubmittableAssignments(Long studyId) {
        List<StudyDetail> studyDetailsWithSubmittableAssignments =
                studyDetailRepository.findAllWithSubmittableAssignmentsByStudyId(studyId);
        return studyDetailsWithSubmittableAssignments.stream()
                .map(AssignmentResponse::from)
                .toList();
    }
}
