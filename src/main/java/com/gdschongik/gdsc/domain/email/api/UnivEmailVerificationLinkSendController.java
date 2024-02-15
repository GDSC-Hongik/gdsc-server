package com.gdschongik.gdsc.domain.email.api;

import com.gdschongik.gdsc.domain.email.application.UnivEmailVerificationLinkSenderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Univ Email Verification Link Send", description = "학교 인증 메일 발송 API입니다.")
@RestController
@RequestMapping("/onboarding/send-verify-email")
@RequiredArgsConstructor
public class UnivEmailVerificationLinkSendController {

    private final UnivEmailVerificationLinkSenderService univEmailVerificationLinkSenderService;

    @Operation(summary = "학교 인증 메일 발송 요청", description = "학교 인증 메일 발송을 요청합니다.")
    @PostMapping
    public ResponseEntity<Void> sendUnivEmailVerificationLink() {
        univEmailVerificationLinkSenderService.send();
        return ResponseEntity.ok().build();
    }
}
