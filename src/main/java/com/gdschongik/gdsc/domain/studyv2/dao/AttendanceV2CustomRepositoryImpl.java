package com.gdschongik.gdsc.domain.studyv2.dao;

import static com.gdschongik.gdsc.domain.studyv2.domain.QAttendanceV2.*;
import static com.gdschongik.gdsc.domain.studyv2.domain.QStudySessionV2.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.domain.AttendanceV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AttendanceV2CustomRepositoryImpl implements AttendanceV2CustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AttendanceV2> findFetchByMemberAndStudy(Member member, StudyV2 study) {
        return queryFactory
                .selectFrom(attendanceV2)
                .innerJoin(attendanceV2.studySession)
                .fetchJoin()
                .where(eqMemberId(member.getId()), eqStudyId(study.getId()))
                .fetch();
    }

    @Override
    public List<AttendanceV2> findByStudyIdAndMemberIds(Long studyId, List<Long> memberIds) {
        return queryFactory
                .selectFrom(attendanceV2)
                .innerJoin(attendanceV2.studySession, studySessionV2)
                .fetchJoin()
                .where(attendanceV2.student.id.in(memberIds), eqStudyId(studyId))
                .fetch();
    }

    private BooleanExpression eqMemberId(Long memberId) {
        return memberId != null ? attendanceV2.student.id.eq(memberId) : null;
    }

    private BooleanExpression eqStudyId(Long studyId) {
        return studyId != null ? attendanceV2.studySession.study.id.eq(studyId) : null;
    }
}
