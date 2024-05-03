package com.gdschongik.gdsc.domain.application.dao;

import static com.gdschongik.gdsc.domain.application.domain.QApplication.application;

import com.gdschongik.gdsc.domain.application.domain.Application;
import com.gdschongik.gdsc.domain.application.domain.dto.request.ApplicationQueryOption;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class ApplicationCustomRepositoryImpl extends ApplicationQueryMethod implements ApplicationCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Application> findAllByPaymentStatus(
            ApplicationQueryOption queryOption, RequirementStatus paymentStatus, Pageable pageable) {
        List<Application> fetch = queryFactory
                .selectFrom(application)
                .where(
                        matchesQueryOption(queryOption),
                        eqRequirementStatus(application.paymentStatus, paymentStatus),
                        isApplicationIdNotNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(application.year.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(application.count())
                .from(application)
                .where(eqRequirementStatus(application.paymentStatus, paymentStatus), isApplicationIdNotNull());

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }
}
