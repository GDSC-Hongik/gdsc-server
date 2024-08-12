package com.gdschongik.gdsc.domain.study.dao;

import static com.gdschongik.gdsc.domain.study.domain.QAssignmentHistory.assignmentHistory;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AssignmentCustomRepositoryImpl implements AssignmentCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AssignmentHistory> findAssignmentHistoriesByMenteeAndStudy(Long studyId, Member currentMember) {
        return queryFactory
                .selectFrom(assignmentHistory)
                .where(assignmentHistory
                        .studyDetail
                        .study
                        .id
                        .eq(studyId)
                        .and(assignmentHistory.member.eq(currentMember)))
                .fetch();
    }
}
