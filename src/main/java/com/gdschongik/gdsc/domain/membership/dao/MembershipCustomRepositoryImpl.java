package com.gdschongik.gdsc.domain.membership.dao;

import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.membership.domain.dto.request.MembershipQueryOption;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.gdschongik.gdsc.domain.membership.domain.QMembership.membership;


@RequiredArgsConstructor
public class MembershipCustomRepositoryImpl extends MembershipQueryMethod implements MembershipCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Membership> findAllByPaymentStatus(
            MembershipQueryOption queryOption, RequirementStatus paymentStatus, Pageable pageable) {
        List<Membership> fetch = queryFactory
                .selectFrom(membership)
                .where(
                        matchesQueryOption(queryOption),
                        eqRequirementStatus(membership.paymentStatus, paymentStatus),
                        isApplicationIdNotNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(membership.academicYear.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(membership.count())
                .from(membership)
                .where(eqRequirementStatus(membership.paymentStatus, paymentStatus), isApplicationIdNotNull());

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }
}
