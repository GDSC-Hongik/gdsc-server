package com.gdschongik.gdsc.domain.study.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import java.util.List;

public interface AssignmentHistoryCustomRepository {

    List<AssignmentHistory> findAssignmentHistoriesByStudentAndStudyId(Member member, Long studyId);

    List<AssignmentHistory> findByStudyIdAndMemberIds(Long studyId, List<Long> memberIds);

    void deleteByStudyIdAndMemberId(Long studyId, Long memberId);
}
