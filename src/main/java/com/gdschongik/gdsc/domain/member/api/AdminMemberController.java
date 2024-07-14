package com.gdschongik.gdsc.domain.member.api;

import com.gdschongik.gdsc.domain.member.application.AdminMemberService;
import com.gdschongik.gdsc.domain.member.dto.request.MemberDemoteRequest;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryOption;
import com.gdschongik.gdsc.domain.member.dto.request.MemberUpdateRequest;
import com.gdschongik.gdsc.domain.member.dto.response.AdminMemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Member", description = "어드민 회원 관리 API입니다.")
@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @Operation(summary = "회원 역할별 목록 조회", description = "정회원, 준회원, 게스트별로 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<AdminMemberResponse>> getMembers(
            @ParameterObject MemberQueryOption queryOption, @ParameterObject Pageable pageable) {
        Page<AdminMemberResponse> response = adminMemberService.findAllByRole(queryOption, pageable);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "회원 탈퇴", description = "회원을 탈퇴시킵니다.")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> withdrawMember(@PathVariable Long memberId) {
        adminMemberService.withdrawMember(memberId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @PutMapping("/{memberId}")
    public ResponseEntity<Void> updateMember(
            @PathVariable Long memberId, @Valid @RequestBody MemberUpdateRequest request) {
        adminMemberService.updateMember(memberId, request);
        return ResponseEntity.ok().build();
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

    @Operation(summary = "정회원 일괄 강등", description = "모든 정회원을 준회원으로 일괄 강등합니다. 리쿠르팅 시작 전에 사용합니다.")
    @PatchMapping("/demotion")
    public ResponseEntity<Void> demoteAllMembersToAssociate(MemberDemoteRequest request) {
        adminMemberService.demoteAllRegularMembersToAssociate(request);
        return ResponseEntity.ok().build();
    }
}
