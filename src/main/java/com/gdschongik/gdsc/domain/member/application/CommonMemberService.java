package com.gdschongik.gdsc.domain.member.application;

import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.dto.response.MemberDepartmentResponse;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonMemberService {

    public List<MemberDepartmentResponse> getDepartments() {
        return Arrays.stream(Department.values())
                .map(MemberDepartmentResponse::from)
                .toList();
    }

    public List<MemberDepartmentResponse> searchDepartments(String departmentName) {
        if (departmentName == null) {
            return getDepartments();
        }

        return Arrays.stream(Department.values())
                .filter(department -> department.getDepartmentName().contains(departmentName))
                .map(MemberDepartmentResponse::from)
                .toList();
    }
}
