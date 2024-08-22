package com.gdschongik.gdsc.domain.study.dao;

import static com.gdschongik.gdsc.domain.member.domain.QMember.member;
import static com.gdschongik.gdsc.domain.study.domain.QAttendance.attendance;
import static com.gdschongik.gdsc.domain.study.domain.QStudyDetail.studyDetail;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.Attendance;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AttendanceCustomRepositoryImpl implements AttendanceCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Attendance> findByMemberAndStudyId(Member member, Long studyId) {
        return queryFactory
                .selectFrom(attendance)
                .leftJoin(attendance.studyDetail, studyDetail)
                .fetchJoin()
                .where(eqMemberId(member.getId()), eqStudyId(studyId))
                .fetch();
    }

    private BooleanExpression eqMemberId(Long memberId) {
        return memberId != null ? attendance.student.id.eq(memberId) : null;
    }

    private BooleanExpression eqStudyId(Long studyId) {
        return studyId != null ? attendance.studyDetail.study.id.eq(studyId) : null;
    }
}
