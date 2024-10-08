package com.gdschongik.gdsc.domain.study.dao;

import static com.gdschongik.gdsc.domain.study.domain.QStudyAchievement.*;

import com.gdschongik.gdsc.domain.study.domain.StudyAchievement;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudyAchievementCustomRepositoryImpl implements StudyAchievementCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StudyAchievement> findByStudyIdAndMemberIds(Long studyId, List<Long> memberIds) {
        return queryFactory
                .selectFrom(studyAchievement)
                .where(eqStudyId(studyId), studyAchievement.student.id.in(memberIds))
                .fetch();
    }

    private BooleanExpression eqStudyId(Long studyId) {
        return studyId != null ? studyAchievement.study.id.eq(studyId) : null;
    }
}
