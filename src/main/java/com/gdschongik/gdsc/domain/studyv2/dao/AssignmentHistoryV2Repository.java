package com.gdschongik.gdsc.domain.studyv2.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.domain.AssignmentHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudySessionV2;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentHistoryV2Repository extends JpaRepository<AssignmentHistoryV2, Long> {
    Optional<AssignmentHistoryV2> findByMemberAndStudySession(Member member, StudySessionV2 studySession);
}
