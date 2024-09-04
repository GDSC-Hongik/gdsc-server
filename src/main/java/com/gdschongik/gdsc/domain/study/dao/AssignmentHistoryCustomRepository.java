package com.gdschongik.gdsc.domain.study.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import java.util.List;

public interface AssignmentHistoryCustomRepository {

    boolean existsSubmittedAssignmentByMemberAndStudy(Member member, Study study);

    List<AssignmentHistory> findAssignmentHistoriesByStudentAndStudyId(Member member, Long studyId);

    void deleteByStudyHistory(StudyHistory studyHistory);
}
