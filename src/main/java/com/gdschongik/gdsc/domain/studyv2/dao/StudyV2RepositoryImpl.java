package com.gdschongik.gdsc.domain.studyv2.dao;

import static com.gdschongik.gdsc.domain.studyv2.domain.QStudyV2.*;

import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudyV2RepositoryImpl implements StudyV2CustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<StudyV2> findFetchById(Long id) {
        return Optional.ofNullable(queryFactory
                .selectFrom(studyV2)
                .join(studyV2.studySessions)
                .fetchJoin()
                .where(studyV2.id.eq(id))
                .fetchOne());
    }

    @Override
    public Optional<StudyV2> findFetchBySessionId(Long sessionId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(studyV2)
                .join(studyV2.studySessions)
                .fetchJoin()
                .where(studyV2.studySessions.any().id.eq(sessionId))
                .fetchOne());
    }

    @Override
    public List<StudyV2> findFetchAll() {
        return queryFactory
                .selectFrom(studyV2)
                .join(studyV2.studySessions)
                .fetchJoin()
                .join(studyV2.mentor)
                .fetchJoin()
                .fetch();
    }
}
