package com.gdschongik.gdsc.domain.member.api;

import com.gdschongik.gdsc.domain.member.application.AdminMemberService;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.domain.member.dto.request.MemberGrantRequest;
import com.gdschongik.gdsc.domain.member.dto.request.MemberPaymentRequest;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryOption;
import com.gdschongik.gdsc.domain.member.dto.request.MemberUpdateRequest;
import com.gdschongik.gdsc.domain.member.dto.response.AdminMemberResponse;
import com.gdschongik.gdsc.domain.member.dto.response.MemberGrantResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Member", description = "어드민 회원 관리 API입니다.")
@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @Operation(summary = "전체 회원 목록 조회", description = "전체 회원 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<AdminMemberResponse>> getMembers(MemberQueryOption queryOption, Pageable pageable) {
        Page<AdminMemberResponse> response = adminMemberService.findAll(queryOption, pageable);
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
    public ResponseEntity<Page<AdminMemberResponse>> getPendingMembers(
            MemberQueryOption queryOption, Pageable pageable) {
        Page<AdminMemberResponse> response = adminMemberService.findAllPendingMembers(queryOption, pageable);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @PutMapping("/{memberId}")
    public ResponseEntity<Void> updateMember(
            @PathVariable Long memberId, @Valid @RequestBody MemberUpdateRequest request) {
        adminMemberService.updateMember(memberId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 승인", description = "회원의 가입을 승인합니다.", deprecated = true)
    @PutMapping("/grant")
    public ResponseEntity<MemberGrantResponse> grantMember(@Valid @RequestBody MemberGrantRequest request) {
        MemberGrantResponse response = adminMemberService.grantMember(request);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "승인 가능 회원 전체 조회", description = "승인 가능한 회원 전체를 조회합니다.", deprecated = true)
    @GetMapping("/grantable")
    public ResponseEntity<Page<AdminMemberResponse>> getGrantableMembers(
            MemberQueryOption queryOption, Pageable pageable) {
        Page<AdminMemberResponse> response = adminMemberService.getGrantableMembers(queryOption, pageable);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "회비 납부 상태에 따른 회원 전체 조회", description = "회비 납부 상태에 따라 회원 목록을 조회합니다.", deprecated = true)
    @GetMapping("/payment")
    public ResponseEntity<Page<AdminMemberResponse>> getMembersByPaymentStatus(
            MemberQueryOption queryOption,
            @RequestParam(name = "status", required = false) RequirementStatus paymentStatus,
            Pageable pageable) {
        Page<AdminMemberResponse> response =
                adminMemberService.getMembersByPaymentStatus(queryOption, paymentStatus, pageable);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "회비 납부 상태 변경", description = "회비 납부 상태를 변경합니다.", deprecated = true)
    @PutMapping("/payment/{memberId}")
    public ResponseEntity<Void> updatePayment(
            @PathVariable Long memberId, @Valid @RequestBody MemberPaymentRequest request) {
        adminMemberService.updatePaymentStatus(memberId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "승인된 회원 전체 조회", description = "승인된 회원 전체를 조회합니다.", deprecated = true)
    @GetMapping("/granted")
    public ResponseEntity<Page<AdminMemberResponse>> getGrantedMembers(
            MemberQueryOption queryOption, Pageable pageable) {
        Page<AdminMemberResponse> response = adminMemberService.findAllGrantedMembers(queryOption, pageable);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "회원 정보 엑셀 다운로드", description = "회원 정보를 엑셀로 다운로드합니다.")
    @GetMapping("/excel")
    public ResponseEntity<byte[]> createWorkbook() throws IOException {
        byte[] response = adminMemberService.createExcel();
        ContentDisposition contentDisposition =
                ContentDisposition.builder("attachment").filename("members.xls").build();
        return ResponseEntity.ok()
                .headers(httpHeaders -> {
                    httpHeaders.setContentDisposition(contentDisposition);
                    httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    httpHeaders.setContentLength(response.length);
                })
                .body(response);
    }
}
