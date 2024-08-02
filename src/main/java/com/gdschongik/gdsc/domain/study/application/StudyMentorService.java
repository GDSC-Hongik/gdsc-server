package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.dto.response.AssignmentResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyMentorService {

    private final StudyDetailRepository studyDetailRepository;

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getWeeklyAssignments(Long studyId) {
        List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyId(studyId);
        return studyDetails.stream().map(AssignmentResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public AssignmentResponse getAssignment(Long studyDetailId) {
        StudyDetail studyDetail = studyDetailRepository
                .findById(studyDetailId)
                .orElseThrow(() -> new CustomException(STUDY_DETAIL_NOT_FOUND));
        return AssignmentResponse.from(studyDetail);
    }
}
