package com.gdschongik.gdsc.domain.study.dao;

import static com.gdschongik.gdsc.domain.study.domain.QStudyDetail.*;

import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudyDetailCustomRepositoryImpl implements StudyDetailCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StudyDetail> findAllSubmittableAssignments(Long studyId) {
        return queryFactory
                .selectFrom(studyDetail)
                .where(eqStudyId(studyId), isSubmittableAssignment())
                .fetch();
    }

    private BooleanExpression eqStudyId(Long studyId) {
        return studyDetail.study.id.eq(studyId);
    }

    private BooleanExpression isSubmittableAssignment() {
        LocalDateTime now = LocalDateTime.now();
        return studyDetail.assignment.deadline.after(now);
    }
}
