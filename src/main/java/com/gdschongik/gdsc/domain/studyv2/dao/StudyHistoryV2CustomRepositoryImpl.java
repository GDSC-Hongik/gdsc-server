package com.gdschongik.gdsc.domain.studyv2.dao;

import static com.gdschongik.gdsc.domain.studyv2.domain.QStudyHistoryV2.*;

import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudyHistoryV2CustomRepositoryImpl implements StudyHistoryV2CustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StudyHistoryV2> findAllByStudyIdAndStudentIds(Long studyId, List<Long> studentIds) {
        return queryFactory
                .selectFrom(studyHistoryV2)
                .where(studyHistoryV2.study.id.eq(studyId), studyHistoryV2.student.id.in(studentIds))
                .fetch();
    }
}
