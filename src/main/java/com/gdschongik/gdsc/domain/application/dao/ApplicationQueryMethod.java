package com.gdschongik.gdsc.domain.application.dao;

import static com.gdschongik.gdsc.domain.application.domain.QApplication.application;

import com.gdschongik.gdsc.domain.application.domain.dto.request.ApplicationQueryOption;
import com.gdschongik.gdsc.domain.common.model.Semester;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;

public class ApplicationQueryMethod {
    protected BooleanExpression eqRequirementStatus(
            EnumPath<RequirementStatus> requirement, RequirementStatus requirementStatus) {
        return requirementStatus != null ? requirement.eq(requirementStatus) : null;
    }

    protected BooleanExpression isApplicationIdNotNull() {
        return application.id.isNotNull();
    }

    protected BooleanExpression eqYear(Integer year) {
        return year != null ? application.year.eq(year) : null;
    }

    protected BooleanExpression eqSemester(Semester semester) {
        return semester != null ? application.semester.eq(semester) : null;
    }

    protected BooleanBuilder matchesQueryOption(ApplicationQueryOption queryOption) {
        return new BooleanBuilder().and(eqYear(queryOption.year())).and(eqSemester(queryOption.sememster()));
    }
}
