package com.gdschongik.gdsc.domain.member.dao;

import static com.gdschongik.gdsc.domain.member.domain.QMember.*;

import com.gdschongik.gdsc.domain.common.model.RequirementStatus;
import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryOption;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import java.util.List;

public interface MemberQueryMethod {

    default BooleanExpression eqRole(MemberRole role) {
        return role != null ? member.role.eq(role) : null;
    }

    default BooleanExpression eqRoles(List<MemberRole> roles) {
        return roles != null && !roles.isEmpty() ? member.role.in(roles) : null;
    }

    default BooleanExpression eqStudentId(String studentId) {
        return studentId != null ? member.studentId.containsIgnoreCase(studentId) : null;
    }

    default BooleanExpression eqName(String name) {
        return name != null ? member.name.containsIgnoreCase(name) : null;
    }

    default BooleanExpression eqPhone(String phone) {
        return phone != null ? member.phone.contains(phone.replaceAll("-", "")) : null;
    }

    default BooleanExpression eqEmail(String email) {
        return email != null ? member.email.containsIgnoreCase(email) : null;
    }

    default BooleanExpression eqDiscordUsername(String discordUsername) {
        return discordUsername != null ? member.discordUsername.containsIgnoreCase(discordUsername) : null;
    }

    default BooleanExpression eqNickname(String nickname) {
        return nickname != null ? member.nickname.containsIgnoreCase(nickname) : null;
    }

    default BooleanExpression eqRequirementStatus(
            EnumPath<RequirementStatus> requirement, RequirementStatus requirementStatus) {
        return requirementStatus != null ? requirement.eq(requirementStatus) : null;
    }

    default BooleanExpression inDepartmentList(List<Department> departmentCodes) {
        return departmentCodes.isEmpty() ? null : member.department.in(departmentCodes);
    }

    default BooleanBuilder matchesQueryOption(MemberQueryOption queryOption) {
        return new BooleanBuilder()
                .and(eqStudentId(queryOption.studentId()))
                .and(eqName(queryOption.name()))
                .and(eqPhone(queryOption.phone()))
                .and(inDepartmentList(Department.searchDepartments(queryOption.department())))
                .and(eqEmail(queryOption.email()))
                .and(eqDiscordUsername(queryOption.discordUsername()))
                .and(eqNickname(queryOption.nickname()))
                .and(eqRoles(queryOption.roles()));
    }
}
