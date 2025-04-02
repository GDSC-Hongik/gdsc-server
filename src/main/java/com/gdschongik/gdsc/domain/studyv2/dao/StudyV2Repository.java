package com.gdschongik.gdsc.domain.studyv2.dao;

import com.gdschongik.gdsc.domain.common.vo.Semester;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyV2Repository extends JpaRepository<StudyV2, Long>, StudyV2CustomRepository {

    List<StudyV2> findAllByMentor(Member mentor);

    Optional<StudyV2> findByTitleAndSemester(String title, Semester semester);
}
