package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Department;

public record MemberDepartmentResponse(Department code, String name) {

    public static MemberDepartmentResponse from(Department department) {
        return new MemberDepartmentResponse(department, department.getDepartmentName());
    }
}
