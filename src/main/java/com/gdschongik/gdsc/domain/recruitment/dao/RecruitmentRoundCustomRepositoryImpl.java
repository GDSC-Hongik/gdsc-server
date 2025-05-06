package com.gdschongik.gdsc.domain.recruitment.dao;

import static com.gdschongik.gdsc.domain.recruitment.domain.QRecruitmentRound.recruitmentRound;

import com.gdschongik.gdsc.domain.common.vo.Semester;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecruitmentRoundCustomRepositoryImpl implements RecruitmentRoundCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RecruitmentRound> findAllBySemester(Semester semester) {
        return queryFactory
                .selectFrom(recruitmentRound)
                .where(recruitmentRound.recruitment.semester.eq(semester))
                .fetch();
    }
}
