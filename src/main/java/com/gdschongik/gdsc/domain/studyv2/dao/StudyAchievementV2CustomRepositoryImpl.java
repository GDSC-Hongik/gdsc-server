package com.gdschongik.gdsc.domain.studyv2.dao;

import static com.gdschongik.gdsc.domain.studyv2.domain.QStudyAchievementV2.*;

import com.gdschongik.gdsc.domain.study.domain.AchievementType;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyAchievementV2;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudyAchievementV2CustomRepositoryImpl implements StudyAchievementV2CustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StudyAchievementV2> findByStudyIdAndMemberIds(Long studyId, List<Long> memberIds) {
        return queryFactory
                .selectFrom(studyAchievementV2)
                .where(eqStudyId(studyId), containsStudentId(memberIds))
                .fetch();
    }

    @Override
    public void deleteByStudyAndAchievementTypeAndMemberIds(
            Long studyId, AchievementType achievementType, List<Long> memberIds) {
        queryFactory
                .delete(studyAchievementV2)
                .where(eqStudyId(studyId), eqAchievementType(achievementType), containsStudentId(memberIds))
                .execute();
    }

    @Override
    public long countByStudyIdAndAchievementTypeAndStudentIds(
            Long studyId, AchievementType achievementType, List<Long> studentIds) {
        return Objects.requireNonNull(queryFactory
                .select(studyAchievementV2.count())
                .from(studyAchievementV2)
                .where(eqStudyId(studyId), eqAchievementType(achievementType), containsStudentId(studentIds))
                .fetchOne());
    }

    private BooleanExpression eqStudyId(Long studyId) {
        return studyId != null ? studyAchievementV2.study.id.eq(studyId) : null;
    }

    private BooleanExpression eqAchievementType(AchievementType achievementType) {
        return achievementType != null ? studyAchievementV2.type.eq(achievementType) : null;
    }

    private BooleanExpression containsStudentId(List<Long> memberIds) {
        return memberIds != null ? studyAchievementV2.student.id.in(memberIds) : null;
    }
}
