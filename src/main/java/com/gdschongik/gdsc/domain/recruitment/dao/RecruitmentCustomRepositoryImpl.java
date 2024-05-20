package com.gdschongik.gdsc.domain.recruitment.dao;

import static com.gdschongik.gdsc.domain.recruitment.domain.QRecruitment.*;

import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecruitmentCustomRepositoryImpl implements RecruitmentCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Recruitment> findOpenRecruitment() {
        return Optional.ofNullable(
                queryFactory.selectFrom(recruitment).where(isOpen()).fetchOne());
    }

    // todo: 대시보드 기능에서 현재 시간을 클라이언트에게 받도록 수정
    private BooleanBuilder isOpen() {
        LocalDateTime now = LocalDateTime.now();
        return new BooleanBuilder().and(recruitment.period.startDate.loe(now)).and(recruitment.period.endDate.goe(now));
    }
}
