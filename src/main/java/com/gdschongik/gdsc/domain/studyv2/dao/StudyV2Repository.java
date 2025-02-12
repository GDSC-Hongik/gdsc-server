package com.gdschongik.gdsc.domain.studyv2.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyV2Repository extends JpaRepository<StudyV2, Long>, StudyV2CustomRepository {

    List<StudyV2> findAllByMentor(Member mentor);
}
