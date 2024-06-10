package com.gdschongik.gdsc.domain.membership.dao;

import static com.gdschongik.gdsc.domain.membership.domain.QMembership.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MembershipCustomRepositoryImpl extends MembershipQueryMethod implements MembershipCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsIssuedMembership(Member member, Integer academicYear, SemesterType semesterType) {
        Integer count = queryFactory
                .selectOne()
                .from(membership)
                .where(
                        eqMember(member),
                        eqAcademicYear(academicYear),
                        eqSemesterType(semesterType),
                        isPaymentVerified())
                .fetchFirst();

        return count != null;
    }
}
