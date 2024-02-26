package com.gdschongik.gdsc.domain.email.api;

import static com.gdschongik.gdsc.global.common.constant.EmailConstant.VERIFY_EMAIL_REQUEST_PARAMETER_KEY;

import com.gdschongik.gdsc.domain.email.application.UnivEmailVerificationLinkSendService;
import com.gdschongik.gdsc.domain.email.application.UnivEmailVerificationService;
import com.gdschongik.gdsc.domain.email.dto.request.UnivEmailVerificationLinkSendRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Univ Email", description = "학교 인증 메일 인증 API입니다.")
@RestController
@RequestMapping("/onboarding")
@RequiredArgsConstructor
public class OnboardingUnivEmailController {

    private final UnivEmailVerificationLinkSendService univEmailVerificationLinkSendService;
    private final UnivEmailVerificationService univEmailVerificationService;

    @Operation(summary = "학교 인증 메일 발송 요청", description = "학교 인증 메일 발송을 요청합니다.")
    @PostMapping("/send-verify-email")
    public ResponseEntity<Void> sendUnivEmailVerificationLink(
            @Valid @RequestBody UnivEmailVerificationLinkSendRequest request) {
        univEmailVerificationLinkSendService.send(request.univEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "학교 인증 메일 인증하기", description = "학교 인증 메일을 인증합니다.")
    @GetMapping("/verify-email")
    public ResponseEntity<Void> sendUnivEmailVerificationLink(
            @RequestParam(VERIFY_EMAIL_REQUEST_PARAMETER_KEY) String verificationCode) {
        univEmailVerificationService.verifyMemberUnivEmail(verificationCode);
        return ResponseEntity.ok().build();
    }
}
