package com.gdschongik.gdsc.domain.application.api;

import com.gdschongik.gdsc.domain.application.application.ApplicationService;
import com.gdschongik.gdsc.domain.application.domain.dto.request.ApplicationQueryOption;
import com.gdschongik.gdsc.domain.application.domain.dto.response.ApplicationResponse;
import com.gdschongik.gdsc.domain.common.model.Semester;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
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

@Tag(name = "Application", description = "가입 신청 API입니다.")
@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @Operation(summary = "회비 납부 상태와 학기에 따른 가입신청 전체 조회", description = "회비 납부 상태와 학기에 따른 가입신청 목록을 조회합니다.")
    @GetMapping("/payment")
    public ResponseEntity<Page<ApplicationResponse>> getMembersByPaymentStatus(
            ApplicationQueryOption queryOption, @RequestParam(name = "status", required = false) RequirementStatus paymentStatus, Pageable pageable) {
        Page<ApplicationResponse> response = applicationService.getApplicationsByPaymentStatus(queryOption, paymentStatus, pageable);
        return ResponseEntity.ok().body(response);
    }
}
