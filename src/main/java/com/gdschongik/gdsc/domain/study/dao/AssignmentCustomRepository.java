package com.gdschongik.gdsc.domain.study.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import java.util.List;

public interface AssignmentCustomRepository {
    List<AssignmentHistory> findAssignmentHistoriesByMenteeAndStudy(Long studyId, Member currentMember);
}
