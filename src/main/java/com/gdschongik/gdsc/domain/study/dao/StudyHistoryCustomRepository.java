package com.gdschongik.gdsc.domain.study.dao;

import java.util.List;

public interface StudyHistoryCustomRepository {

    Long countByStudyIdAndStudentIds(Long studyId, List<Long> studentIds);
}
