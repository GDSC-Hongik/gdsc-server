package com.gdschongik.gdsc.domain.study.dao;

import static com.gdschongik.gdsc.domain.study.domain.QStudyHistory.*;

import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudyHistoryCustomRepositoryImpl implements StudyHistoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countByStudyIdAndStudentIds(Long studyId, List<Long> studentIds) {
        return Objects.requireNonNull(queryFactory
                .select(studyHistory.count())
                .from(studyHistory)
                .where(eqStudyId(studyId), studyHistory.student.id.in(studentIds))
                .fetchOne());
    }

    @Override
    public List<StudyHistory> findAllByStudyIdAndStudentIds(Long studyId, List<Long> studentIds) {
        return queryFactory
                .selectFrom(studyHistory)
                .where(eqStudyId(studyId), studyHistory.student.id.in(studentIds))
                .fetch();
    }

    private BooleanExpression eqStudyId(Long studyId) {
        return studyHistory.study.id.eq(studyId);
    }
}
