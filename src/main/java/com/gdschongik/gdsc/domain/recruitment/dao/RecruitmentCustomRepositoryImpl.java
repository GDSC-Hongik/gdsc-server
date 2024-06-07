package com.gdschongik.gdsc.domain.recruitment.dao;

import static com.gdschongik.gdsc.domain.recruitment.domain.QRecruitment.*;

import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentQueryOption;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class RecruitmentCustomRepositoryImpl extends RecruitmentQueryMethod implements RecruitmentCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Recruitment> findAllOrderByStartDate(RecruitmentQueryOption queryOption, Pageable pageable) {
        List<Recruitment> recruitments = queryFactory
                .selectFrom(recruitment)
                .where(matchesQueryOption(queryOption))
                .orderBy(recruitment.period.startDate.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery =
                queryFactory.select(recruitment.count()).from(recruitment).where(matchesQueryOption(queryOption));

        return PageableExecutionUtils.getPage(recruitments, pageable, countQuery::fetchOne);
    }
}
