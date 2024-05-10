package com.gdschongik.gdsc.domain.membership.dao;


import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.domain.membership.domain.dto.request.MembershipQueryOption;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;

import static com.gdschongik.gdsc.domain.membership.domain.QMembership.membership;


public class MembershipQueryMethod {
    protected BooleanExpression eqRequirementStatus(
            EnumPath<RequirementStatus> requirement, RequirementStatus requirementStatus) {
        return requirementStatus != null ? requirement.eq(requirementStatus) : null;
    }

    protected BooleanExpression isApplicationIdNotNull() {
        return membership.id.isNotNull();
    }

    protected BooleanExpression eqAcademicYear(Integer year) {
        return year != null ? membership.academicYear.eq(year) : null;
    }

    protected BooleanExpression eqSemesterType(SemesterType semesterType) {
        return semesterType != null ? membership.semesterType.eq(semesterType) : null;
    }

    protected BooleanBuilder matchesQueryOption(MembershipQueryOption queryOption) {
        return new BooleanBuilder().and(eqAcademicYear(queryOption.year())).and(eqSemesterType(queryOption.semesterType()));
    }
}
