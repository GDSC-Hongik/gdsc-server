package com.gdschongik.gdsc.domain.membership.dao;

import static com.gdschongik.gdsc.domain.membership.domain.QMembership.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MembershipCustomRepositoryImpl implements MembershipCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByMemberAndRecruitment(Member member, Recruitment recruitment) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(membership)
                .where(membership.member.eq(member), membership.recruitmentRound.recruitment.eq(recruitment))
                .fetchFirst();

        return fetchOne != null;
    }
}
