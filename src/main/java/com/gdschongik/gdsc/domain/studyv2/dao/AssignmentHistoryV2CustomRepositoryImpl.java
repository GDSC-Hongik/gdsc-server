package com.gdschongik.gdsc.domain.studyv2.dao;

import static com.gdschongik.gdsc.domain.studyv2.domain.QAssignmentHistoryV2.*;
import static com.gdschongik.gdsc.domain.studyv2.domain.QStudySessionV2.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.domain.AssignmentHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AssignmentHistoryV2CustomRepositoryImpl implements AssignmentHistoryV2CustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AssignmentHistoryV2> findByMemberAndStudy(Member member, StudyV2 study) {
        return queryFactory
                .selectFrom(assignmentHistoryV2)
                .innerJoin(assignmentHistoryV2.studySession)
                .where(eqMemberId(member.getId()).and(eqStudyId(study.getId())))
                .fetch();
    }

    @Override
    public List<AssignmentHistoryV2> findByStudyIdAndMemberIds(Long studyId, List<Long> memberIds) {
        return queryFactory
                .selectFrom(assignmentHistoryV2)
                .innerJoin(assignmentHistoryV2.studySession, studySessionV2)
                .fetchJoin()
                .where(assignmentHistoryV2.member.id.in(memberIds), eqStudyId(studyId))
                .fetch();
    }

    private BooleanExpression eqMemberId(Long memberId) {
        return memberId != null ? assignmentHistoryV2.member.id.eq(memberId) : null;
    }

    private BooleanExpression eqStudyId(Long studyId) {
        return studyId != null ? assignmentHistoryV2.studySession.study.id.eq(studyId) : null;
    }
}
