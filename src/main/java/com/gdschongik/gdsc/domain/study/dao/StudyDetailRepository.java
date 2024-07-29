package com.gdschongik.gdsc.domain.study.dao;

import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.vo.Assignment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyDetailRepository extends JpaRepository<StudyDetail, Long> {
    Optional<Assignment> findByAssignmentId(Long assignmentId);
}
