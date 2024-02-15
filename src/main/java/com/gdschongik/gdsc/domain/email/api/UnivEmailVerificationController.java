package com.gdschongik.gdsc.domain.email.api;

import static com.gdschongik.gdsc.domain.email.constant.UnivMailVerificationConstant.VERIFY_EMAIL_REQUEST_PARAMETER_KEY;

import com.gdschongik.gdsc.domain.email.application.UnivEmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Univ Email Verification", description = "학교 인증 메일 인증 API입니다.")
@RestController
@RequestMapping("/onboarding/verify-email")
@RequiredArgsConstructor
public class UnivEmailVerificationController {

    private final UnivEmailVerificationService univEmailVerificationService;

    @Operation(summary = "학교 인증 메일 인증하기", description = "학교 인증 메일을 인증합니다.")
    @GetMapping
    public ResponseEntity<Void> sendUnivEmailVerificationLink(
            @RequestParam(VERIFY_EMAIL_REQUEST_PARAMETER_KEY) String verificationCode) {
        univEmailVerificationService.verifyMemberUnivEmail(verificationCode);
        return ResponseEntity.ok().build();
    }
}
