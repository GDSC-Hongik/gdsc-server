package com.gdschongik.gdsc.domain.study.dao;

import static com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionStatus.*;
import static com.gdschongik.gdsc.domain.study.domain.QAssignmentHistory.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AssignmentHistoryCustomRepositoryImpl implements AssignmentHistoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsSubmittedAssignment(Member member, Study study) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(assignmentHistory)
                .where(
                        assignmentHistory.studyDetail.study.eq(study),
                        assignmentHistory.submissionStatus.in(FAILURE, SUCCESS))
                .fetchFirst();

        return fetchOne != null;
    }
}
