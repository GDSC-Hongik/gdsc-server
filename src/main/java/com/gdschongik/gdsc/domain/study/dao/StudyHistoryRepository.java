package com.gdschongik.gdsc.domain.study.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyHistoryRepository extends JpaRepository<StudyHistory, Long> {

    // TODO mentee -> student로 변경
    List<StudyHistory> findAllByMentee(Member member);

    Optional<StudyHistory> findByMenteeAndStudy(Member member, Study study);

    Optional<StudyHistory> findByMenteeAndStudyId(Member member, Long studyId);
}
