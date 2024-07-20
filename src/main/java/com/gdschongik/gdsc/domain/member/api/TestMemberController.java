package com.gdschongik.gdsc.domain.member.api;

import com.gdschongik.gdsc.domain.member.application.OnboardingMemberService;
import com.gdschongik.gdsc.domain.member.dto.request.MemberTokenRequest;
import com.gdschongik.gdsc.domain.member.dto.response.MemberTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Test Member", description = "회원 테스트용 API입니다. dev 환경에서만 사용 가능합니다")
@RestController
@RequestMapping("/test/members")
@RequiredArgsConstructor
public class TestMemberController {

    private final OnboardingMemberService onboardingMemberService;

    @Operation(summary = "임시 토큰 생성", description = "테스트용 API입니다. oauth_id를 입력받아 해당하는 유저의 토큰을 생성합니다.")
    @PostMapping("/token")
    public ResponseEntity<MemberTokenResponse> createTemporaryToken(@Valid @RequestBody MemberTokenRequest request) {
        MemberTokenResponse response = onboardingMemberService.createTemporaryToken(request);
        return ResponseEntity.ok().body(response);
    }
}
