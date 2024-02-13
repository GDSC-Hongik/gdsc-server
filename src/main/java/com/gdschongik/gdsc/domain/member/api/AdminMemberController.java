package com.gdschongik.gdsc.domain.member.api;

import com.gdschongik.gdsc.domain.member.application.AdminMemberService;
import com.gdschongik.gdsc.domain.member.dto.request.MemberGrantRequest;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryRequest;
import com.gdschongik.gdsc.domain.member.dto.request.MemberUpdateRequest;
import com.gdschongik.gdsc.domain.member.dto.response.MemberFindAllResponse;
import com.gdschongik.gdsc.domain.member.dto.response.MemberGrantResponse;
import com.gdschongik.gdsc.domain.member.dto.response.MemberPendingFindAllResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Member", description = "어드민 회원 관리 API입니다.")
@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @Operation(summary = "전체 회원 목록 조회", description = "전체 회원 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<MemberFindAllResponse>> getMembers(MemberQueryRequest queryRequest, Pageable pageable) {
        Page<MemberFindAllResponse> response = adminMemberService.findAll(queryRequest, pageable);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "회원 탈퇴", description = "회원을 탈퇴시킵니다.")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> withdrawMember(@PathVariable Long memberId) {
        adminMemberService.withdrawMember(memberId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "대기중인 회원 목록 조회", description = "대기중인 회원 목록을 조회합니다.")
    @GetMapping("/pending")
    public ResponseEntity<Page<MemberPendingFindAllResponse>> getPendingMembers(Pageable pageable) {
        Page<MemberPendingFindAllResponse> response = adminMemberService.findAllPendingMembers(pageable);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @PutMapping("/{memberId}")
    public ResponseEntity<Void> updateMember(
            @PathVariable Long memberId, @Valid @RequestBody MemberUpdateRequest request) {
        adminMemberService.updateMember(memberId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 승인", description = "회원의 가입을 승인합니다.")
    @PutMapping("/grant")
    public ResponseEntity<MemberGrantResponse> grantMember(@Valid @RequestBody MemberGrantRequest request) {
        MemberGrantResponse response = adminMemberService.grantMember(request);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "승인 가능 회원 전체 조회", description = "승인 가능한 회원 전체를 조회합니다.")
    @GetMapping("/grantable")
    public ResponseEntity<Page<MemberFindAllResponse>> getGrantableMembers(Pageable pageable) {
        Page<MemberFindAllResponse> response = adminMemberService.getGrantableMembers(pageable);
        return ResponseEntity.ok().body(response);
    }
}
