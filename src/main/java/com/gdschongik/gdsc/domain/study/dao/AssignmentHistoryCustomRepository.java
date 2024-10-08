package com.gdschongik.gdsc.domain.study.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import com.gdschongik.gdsc.domain.study.domain.Study;
import java.util.List;

public interface AssignmentHistoryCustomRepository {

    boolean existsSubmittedAssignmentByMemberAndStudy(Member member, Study study);

    List<AssignmentHistory> findAssignmentHistoriesByStudentAndStudyId(Member member, Long studyId);

    List<AssignmentHistory> findByStudyIdAndMemberIds(Long studyId, List<Long> memberIds);

    void deleteByStudyIdAndMemberId(Long studyId, Long memberId);
}
