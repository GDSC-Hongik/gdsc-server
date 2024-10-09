package com.gdschongik.gdsc.domain.study.dao;

import java.util.List;

public interface StudyHistoryCustomRepository {

    boolean existsByStudyIdAndStudentIds(Long studyId, List<Long> studentIds);
}
