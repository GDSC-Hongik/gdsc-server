package com.gdschongik.gdsc.domain.study.dao;

import static com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionStatus.*;
import static com.gdschongik.gdsc.domain.study.domain.QAssignmentHistory.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AssignmentHistoryCustomRepositoryImpl implements AssignmentHistoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsSubmittedAssignmentByMemberAndStudy(Member member, Study study) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(assignmentHistory)
                .where(eqMember(member), eqStudy(study), isSubmitted())
                .fetchFirst();

        return fetchOne != null;
    }

    private BooleanExpression eqMember(Member member) {
        return member == null ? null : assignmentHistory.member.eq(member);
    }

    private BooleanExpression eqStudy(Study study) {
        return study == null ? null : assignmentHistory.studyDetail.study.eq(study);
    }

    private BooleanExpression isSubmitted() {
        return assignmentHistory.submissionStatus.in(FAILURE, SUCCESS);
    }
}
