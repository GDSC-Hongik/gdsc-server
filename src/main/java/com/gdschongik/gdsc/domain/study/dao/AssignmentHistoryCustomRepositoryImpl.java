package com.gdschongik.gdsc.domain.study.dao;

import static com.gdschongik.gdsc.domain.study.domain.QAssignmentHistory.*;
import static com.gdschongik.gdsc.domain.study.domain.QStudyDetail.studyDetail;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AssignmentHistoryCustomRepositoryImpl
        implements AssignmentHistoryCustomRepository, AssignmentHistoryQueryMethod {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AssignmentHistory> findAssignmentHistoriesByStudentAndStudyId(Member currentMember, Long studyId) {
        return queryFactory
                .selectFrom(assignmentHistory)
                .join(assignmentHistory.studyDetail, studyDetail)
                .fetchJoin()
                .where(eqStudyId(studyId).and(eqMember(currentMember)))
                .fetch();
    }

    @Override
    public void deleteByStudyIdAndMemberId(Long studyId, Long memberId) {
        queryFactory
                .delete(assignmentHistory)
                .where(eqMemberId(memberId), eqStudyId(studyId))
                .execute();
    }

    @Override
    public List<AssignmentHistory> findByStudyIdAndMemberIds(Long studyId, List<Long> memberIds) {
        return queryFactory
                .selectFrom(assignmentHistory)
                .innerJoin(assignmentHistory.studyDetail, studyDetail)
                .fetchJoin()
                .where(assignmentHistory.member.id.in(memberIds), eqStudyId(studyId))
                .fetch();
    }
}
