package com.gdschongik.gdsc.domain.study.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import com.gdschongik.gdsc.domain.study.domain.Study;
import java.util.List;

public interface AssignmentHistoryCustomRepository {

    boolean existsSubmittedAssignmentByMemberAndStudy(Member member, Study study);

    List<AssignmentHistory> findAssignmentHistoriesByMenteeAndStudyId(Member member, Long studyId);
}
