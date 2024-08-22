package com.gdschongik.gdsc.domain.study.dao;

import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyDetailRepository extends JpaRepository<StudyDetail, Long> {

    List<StudyDetail> findAllByStudyIdOrderByWeekAsc(Long studyId);
}
