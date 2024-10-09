package com.gdschongik.gdsc.domain.study.dao;

import static com.gdschongik.gdsc.domain.study.domain.QStudyHistory.*;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudyHistoryCustomRepositoryImpl implements StudyHistoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long countByStudyIdAndStudentIds(Long studyId, List<Long> studentIds) {
        return queryFactory
                .select(studyHistory.count())
                .from(studyHistory)
                .where(eqStudyId(studyId), studyHistory.student.id.in(studentIds))
                .fetchOne();
    }

    private BooleanExpression eqStudyId(Long studyId) {
        return studyHistory.study.id.eq(studyId);
    }
}
