package com.gdschongik.gdsc.domain.member.api;

import com.gdschongik.gdsc.domain.member.application.OnboardingMemberService;
import com.gdschongik.gdsc.domain.member.dto.request.MemberSignupRequest;
import com.gdschongik.gdsc.domain.member.dto.request.OnboardingMemberUpdateRequest;
import com.gdschongik.gdsc.domain.member.dto.response.MemberInfoResponse;
import com.gdschongik.gdsc.domain.member.dto.response.MemberUnivStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @Deprecated
    @Operation(summary = "디스코드 회원 정보 수정", description = "디스코드 회원 정보를 수정합니다.")
    @PutMapping("/me/discord")
    public ResponseEntity<Void> updateMember(@Valid @RequestBody OnboardingMemberUpdateRequest request) {
        onboardingMemberService.updateMember(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponse> getMemberInfo() {
        MemberInfoResponse response = onboardingMemberService.getMemberInfo();
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "재학생 인증 여부 확인", description = "재학생 인증 여부를 확인합니다.")
    @GetMapping("/me/univ-verification")
    public ResponseEntity<MemberUnivStatusResponse> checkUnivVerification() {
        MemberUnivStatusResponse response = onboardingMemberService.checkUnivVerificationStatus();
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "GDSC Bevy 연동하기", description = "GDSC Bevy 가입 상태를 업데이트합니다.")
    @PostMapping("/me/link-bevy")
    public ResponseEntity<Void> linkBevy() {
        onboardingMemberService.verifyBevyStatus();
        return ResponseEntity.ok().build();
    }
}
