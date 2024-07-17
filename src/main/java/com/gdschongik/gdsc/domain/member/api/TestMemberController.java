package com.gdschongik.gdsc.domain.member.api;

import com.gdschongik.gdsc.domain.member.application.AdminMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Test Member", description = "회원 테스트용 API입니다.")
@RestController
@RequestMapping("/test/member")
@RequiredArgsConstructor
public class TestMemberController {

    private final AdminMemberService adminMemberService;

    @Operation(summary = "비회원으로 강등", description = "테스트용 API입니다. 현재 회원 상태를 비회원으로 강등시키기 위해 사용합니다.")
    @PatchMapping("/demotion")
    public ResponseEntity<Void> demoteToGuest() {
        adminMemberService.demoteToGuest();
        return ResponseEntity.ok().build();
    }
}
