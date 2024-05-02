package com.gdschongik.gdsc.domain.application.dao;

import com.gdschongik.gdsc.domain.application.domain.QApplication;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;

public class ApplicationQueryMethod {
    protected BooleanExpression eqRequirementStatus(
            EnumPath<RequirementStatus> requirement, RequirementStatus requirementStatus) {
        return requirementStatus != null ? requirement.eq(requirementStatus) : null;
    }

    protected BooleanExpression isApplicationIdNotNull() {
        return QApplication.application.id.isNotNull();
    }
}
