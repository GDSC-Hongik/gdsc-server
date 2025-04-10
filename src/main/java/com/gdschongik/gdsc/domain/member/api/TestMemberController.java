package com.gdschongik.gdsc.domain.member.api;

import com.gdschongik.gdsc.domain.member.application.AdminMemberService;
import com.gdschongik.gdsc.domain.member.application.OnboardingMemberService;
import com.gdschongik.gdsc.domain.member.application.TestMemberService;
import com.gdschongik.gdsc.domain.member.dto.request.MemberTokenByGithubHandleRequest;
import com.gdschongik.gdsc.domain.member.dto.response.MemberTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Test Member", description = "회원 테스트용 API입니다. dev 환경에서만 사용 가능합니다")
@RestController
@RequestMapping("/test/members")
@RequiredArgsConstructor
public class TestMemberController {

    private final TestMemberService testMemberService;
    private final OnboardingMemberService onboardingMemberService;
    private final AdminMemberService adminMemberService;

    @Operation(summary = "게스트 회원 생성", description = "테스트용 API입니다. 깃허브 핸들명을 입력받아 임시 회원을 생성합니다.")
    @PostMapping
    public ResponseEntity<Void> createTemporaryMember(@RequestParam("handle") String githubHandle) {
        testMemberService.createTestMember(githubHandle);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "깃허브 핸들 이용 임시 토큰 생성", description = "테스트용 API입니다. 깃허브 핸들명을 입력받아 해당하는 유저의 토큰을 생성합니다.")
    @PostMapping("/token/github-handle")
    public ResponseEntity<MemberTokenResponse> createTemporaryTokenByGithubHandle(
            @Valid @RequestBody MemberTokenByGithubHandleRequest request) {
        var response = onboardingMemberService.createTemporaryTokenByGithubHandle(request);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "게스트로 강등", description = "테스트용 API입니다. 현재 멤버 역할을 게스트로 강등시키기 위해 사용합니다.")
    @PatchMapping("/demotion")
    public ResponseEntity<Void> demoteToGuest() {
        adminMemberService.demoteToGuestAndRegularRequirementToUnsatisfied();
        return ResponseEntity.ok().build();
    }
}
