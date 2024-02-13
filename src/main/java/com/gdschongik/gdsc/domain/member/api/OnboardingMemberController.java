package com.gdschongik.gdsc.domain.member.api;

import com.gdschongik.gdsc.domain.member.application.OnboardingMemberService;
import com.gdschongik.gdsc.domain.member.dto.request.MemberSignupRequest;
import com.gdschongik.gdsc.domain.member.dto.response.MemberInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Onboarding Member", description = "회원 온보딩 API입니다.")
@RestController
@RequestMapping("/onboarding/members")
@RequiredArgsConstructor
public class OnboardingMemberController {

    private final OnboardingMemberService onboardingMemberService;

    @Operation(summary = "회원 가입 신청", description = "회원 가입을 신청합니다.")
    @PostMapping
    public ResponseEntity<Void> signupMember(@Valid @RequestBody MemberSignupRequest request) {
        onboardingMemberService.signupMember(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponse> getMemberInfo() {
        MemberInfoResponse response = onboardingMemberService.getMemberInfo();
        return ResponseEntity.ok().body(response);
    }
}
