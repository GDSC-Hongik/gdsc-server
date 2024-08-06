package com.gdschongik.gdsc.domain.study.dao;

import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import java.util.List;

public interface StudyDetailCustomRepository {

    List<StudyDetail> findAllSubmittableAssignments(Long studyId);
}
