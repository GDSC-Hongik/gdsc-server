package com.gdschongik.gdsc.domain.membership.api;

import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.domain.membership.application.AdminMembershipService;
import com.gdschongik.gdsc.domain.membership.domain.dto.request.MembershipQueryOption;
import com.gdschongik.gdsc.domain.membership.domain.dto.response.MembershipResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AdminMembership", description = "어드민 멤버십 API입니다.")
@RestController
@RequestMapping("/admin/membership")
@RequiredArgsConstructor
public class AdminMembershipController {
    private final AdminMembershipService adminMembershipService;

    @Operation(summary = "회비 납부 상태와 학기에 따른 멤버십 가입신청 전체 조회", description = "회비 납부 상태와 학기에 따른 멤버십 가입신청 목록을 조회합니다.")
    @GetMapping("/payment")
    public ResponseEntity<Page<MembershipResponse>> getMembershipByPaymentStatus(
            MembershipQueryOption queryOption,
            @RequestParam(name = "status", required = false) RequirementStatus paymentStatus,
            Pageable pageable) {
        Page<MembershipResponse> response =
                adminMembershipService.getMembershipByPaymentStatus(queryOption, paymentStatus, pageable);
        return ResponseEntity.ok().body(response);
    }
}
