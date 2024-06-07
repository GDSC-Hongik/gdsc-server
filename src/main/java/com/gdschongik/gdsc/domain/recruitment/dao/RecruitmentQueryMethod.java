package com.gdschongik.gdsc.domain.recruitment.dao;

import static com.gdschongik.gdsc.domain.recruitment.domain.QRecruitment.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentQueryOption;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

public class RecruitmentQueryMethod {
    protected BooleanExpression eqAcademicYear(Integer academicYear) {
        return academicYear != null ? recruitment.academicYear.eq(academicYear) : null;
    }

    protected BooleanExpression eqSemesterType(SemesterType semesterType) {
        return semesterType != null ? recruitment.semesterType.eq(semesterType) : null;
    }

    protected BooleanBuilder matchesQueryOption(RecruitmentQueryOption queryOption) {
        return new BooleanBuilder()
                .and(eqAcademicYear(queryOption.academicYear()))
                .and(eqSemesterType(queryOption.semesterType()));
    }
}
