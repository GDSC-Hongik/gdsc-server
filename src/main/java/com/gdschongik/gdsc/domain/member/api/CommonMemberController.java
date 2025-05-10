package com.gdschongik.gdsc.domain.member.api;

import com.gdschongik.gdsc.domain.member.application.CommonMemberService;
import com.gdschongik.gdsc.domain.member.dto.response.MemberAccountInfoResponse;
import com.gdschongik.gdsc.domain.member.dto.response.MemberDepartmentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member - common", description = "공통 멤버 API입니다.")
@RestController
@RequestMapping("/common/members")
@RequiredArgsConstructor
public class CommonMemberController {

    private final CommonMemberService commonMemberService;

    @Operation(summary = "학과 목록 조회", description = "학과 목록을 조회합니다.")
    @GetMapping("/departments")
    public ResponseEntity<List<MemberDepartmentResponse>> getDepartments() {
        List<MemberDepartmentResponse> response = commonMemberService.getDepartments();
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "학과 검색하기", description = "학과를 검색합니다.")
    @GetMapping("/departments/search")
    public ResponseEntity<List<MemberDepartmentResponse>> searchDepartments(
            @RequestParam(name = "department", required = false) String departmentName) {
        List<MemberDepartmentResponse> response = commonMemberService.searchDepartments(departmentName);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "내 계정 정보 조회", description = "내 계정 정보를 조회합니다.")
    @GetMapping("/me/account-info")
    public ResponseEntity<MemberAccountInfoResponse> getAccountInfo() {
        MemberAccountInfoResponse response = commonMemberService.getAccountInfo();
        return ResponseEntity.ok().body(response);
    }
}
