package com.gdschongik.gdsc.domain.recruitment.dao;

import static com.gdschongik.gdsc.domain.recruitment.domain.QRecruitment.*;

import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecruitmentCustomRepositoryImpl implements RecruitmentCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Recruitment> findAllOrderByStartDate() {
        return queryFactory
                .selectFrom(recruitment)
                .orderBy(recruitment.period.startDate.desc())
                .fetch();
    }
}
