package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.ASSIGNMENT_NOT_FOUND;

import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.domain.request.AssignmentCreateRequest;
import com.gdschongik.gdsc.domain.study.domain.vo.Assignment;
import com.gdschongik.gdsc.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyMentorService {

    private final StudyDetailRepository studyDetailRepository;

    @Transactional
    public void createStudyAssignment(Long assignmentId, AssignmentCreateRequest request) {
        Assignment assignment = studyDetailRepository
                .findByAssignmentId(assignmentId)
                .orElseThrow(() -> new CustomException(ASSIGNMENT_NOT_FOUND));

        assignment.update(request.title(), request.deadLine(), request.descriptionNotionLink());
    }
}
