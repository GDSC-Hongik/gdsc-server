package com.gdschongik.gdsc.domain.studyv2.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyHistoryV2Repository extends JpaRepository<StudyHistoryV2, Long>, StudyHistoryV2CustomRepository {
    Optional<StudyHistoryV2> findByStudentAndStudy(Member student, StudyV2 study);
}
