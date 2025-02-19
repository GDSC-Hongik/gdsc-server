package com.gdschongik.gdsc.domain.studyv2.dao;

import static com.gdschongik.gdsc.domain.studyv2.domain.QStudyHistoryV2.*;

import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudyHistoryV2CustomRepositoryImpl implements StudyHistoryV2CustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countByStudyIdAndStudentIds(Long studyId, List<Long> studentIds) {
        return Objects.requireNonNull(queryFactory
                .select(studyHistoryV2.count())
                .from(studyHistoryV2)
                .where(eqStudyId(studyId), studyHistoryV2.student.id.in(studentIds))
                .fetchOne());
    }

    @Override
    public List<StudyHistoryV2> findAllByStudyIdAndStudentIds(Long studyId, List<Long> studentIds) {
        return queryFactory
                .selectFrom(studyHistoryV2)
                .where(eqStudyId(studyId), studyHistoryV2.student.id.in(studentIds))
                .fetch();
    }

    private BooleanExpression eqStudyId(Long studyId) {
        return studyHistoryV2.study.id.eq(studyId);
    }
}
