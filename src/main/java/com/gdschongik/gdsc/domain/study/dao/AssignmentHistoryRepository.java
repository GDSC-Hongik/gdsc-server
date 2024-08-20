package com.gdschongik.gdsc.domain.study.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentHistoryRepository
        extends JpaRepository<AssignmentHistory, Long>, AssignmentHistoryCustomRepository {
    public Optional<AssignmentHistory> findByMemberAndStudyDetail(Member member, StudyDetail studyDetail);

    List<AssignmentHistory> findAssignmentHistoriesByMenteeAndStudyId(Member currentMember, Long studyId);
}
