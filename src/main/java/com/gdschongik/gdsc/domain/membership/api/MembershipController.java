package com.gdschongik.gdsc.domain.membership.api;

import com.gdschongik.gdsc.domain.membership.application.MembershipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Membership", description = "멤버십 API입니다.")
@RestController
@RequestMapping("/membership")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @Operation(summary = "멤버십 가입 신청 접수", description = "멤버십 가입 신청을 접수합니다.")
    @PostMapping
    public ResponseEntity<Void> receiveMembership(@RequestParam(name = "recruitmentId") Long recruitmentId) {
        membershipService.receiveMembership(recruitmentId);
        return ResponseEntity.ok().build();
    }
}
