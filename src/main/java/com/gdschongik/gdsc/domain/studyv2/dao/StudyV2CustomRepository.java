package com.gdschongik.gdsc.domain.studyv2.dao;

import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import java.util.List;
import java.util.Optional;

public interface StudyV2CustomRepository {
    Optional<StudyV2> findFetchById(Long id);

    List<StudyV2> findFetchAll();
}
