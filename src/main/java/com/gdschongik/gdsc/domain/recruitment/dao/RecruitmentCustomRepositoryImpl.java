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
    public List<Recruitment> findAllOrderByAcademicYearAndSemesterTypeAndStartDate() {
        return queryFactory
                .selectFrom(recruitment)
                .orderBy(
                        recruitment.academicYear.desc(),
                        recruitment.semesterType.desc(),
                        recruitment.period.startDate.asc())
                .fetch();
    }
}
