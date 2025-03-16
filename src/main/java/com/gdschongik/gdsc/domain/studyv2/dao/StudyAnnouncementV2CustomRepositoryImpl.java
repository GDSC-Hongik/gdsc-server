package com.gdschongik.gdsc.domain.studyv2.dao;

import static com.gdschongik.gdsc.domain.studyv2.domain.QStudyAnnouncementV2.*;

import com.gdschongik.gdsc.domain.studyv2.domain.StudyAnnouncementV2;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudyAnnouncementV2CustomRepositoryImpl implements StudyAnnouncementV2CustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StudyAnnouncementV2> findAllByStudyIdsOrderByCreatedAtDesc(List<Long> studyIds) {
        return queryFactory
                .selectFrom(studyAnnouncementV2)
                .where(studyAnnouncementV2.study.id.in(studyIds))
                .orderBy(studyAnnouncementV2.createdAt.desc())
                .fetch();
    }
}
