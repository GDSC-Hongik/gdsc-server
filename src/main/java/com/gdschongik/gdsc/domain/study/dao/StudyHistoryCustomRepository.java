package com.gdschongik.gdsc.domain.study.dao;

import java.util.List;

public interface StudyHistoryCustomRepository {

    long countByStudyIdAndStudentIds(Long studyId, List<Long> studentIds);
}
