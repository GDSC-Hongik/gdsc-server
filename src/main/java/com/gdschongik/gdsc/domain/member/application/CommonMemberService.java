package com.gdschongik.gdsc.domain.member.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.dto.response.MemberDepartmentResponse;
import com.gdschongik.gdsc.domain.membership.dao.MembershipRepository;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommonMemberService {

    private final MembershipRepository membershipRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<MemberDepartmentResponse> getDepartments() {
        return Arrays.stream(Department.values())
                .map(MemberDepartmentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
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
