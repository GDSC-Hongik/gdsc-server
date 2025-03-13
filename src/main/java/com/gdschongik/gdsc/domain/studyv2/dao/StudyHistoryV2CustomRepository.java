package com.gdschongik.gdsc.domain.studyv2.dao;

import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;
import java.util.List;

public interface StudyHistoryV2CustomRepository {

    long countByStudyIdAndStudentIds(Long studyId, List<Long> studentIds);

    List<StudyHistoryV2> findAllByStudyIdAndStudentIds(Long studyId, List<Long> studentIds);

    boolean existsByStudyId(Long studyId);
}
