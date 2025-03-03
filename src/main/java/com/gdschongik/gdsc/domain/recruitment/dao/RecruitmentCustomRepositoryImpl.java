package com.gdschongik.gdsc.domain.recruitment.dao;

import static com.gdschongik.gdsc.domain.recruitment.domain.QRecruitment.recruitment;

import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecruitmentCustomRepositoryImpl implements RecruitmentCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Recruitment> findBySemesterPeriodContains(LocalDateTime now) {
        queryFactory
                .selectFrom(recruitment)
                // .where(recruitment.semesterPeriod.startDate.loe(now), recruitment.semesterPeriod.endDate.goe(now))
                .fetchOne();

        return Optional.empty();
    }
}
