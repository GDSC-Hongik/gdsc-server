package com.gdschongik.gdsc.domain.studyv2.dao;

import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyHistoryV2Repository extends JpaRepository<StudyHistoryV2, Long>, StudyHistoryV2CustomRepository {}
