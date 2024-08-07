package com.gdschongik.gdsc.domain.study.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.Study;

public interface AssignmentHistoryCustomRepository {

    boolean existsSubmittedAssignmentByMemberAndStudy(Member member, Study study);
}
