package com.gdschongik.gdsc.domain.study.dao;

import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import java.util.List;

public interface StudyHistoryCustomRepository {

    long countByStudyIdAndStudentIds(Long studyId, List<Long> studentIds);

    List<StudyHistory> findAllByStudyIdAndStudentIds(Long studyId, List<Long> studentIds);
}
