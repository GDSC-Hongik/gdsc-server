package com.gdschongik.gdsc.domain.member.api;

import com.gdschongik.gdsc.domain.member.application.MemberService;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryRequest;
import com.gdschongik.gdsc.domain.member.dto.response.MemberFindAllResponse;
import com.gdschongik.gdsc.domain.member.dto.response.MemberPendingFindAllResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<Page<MemberFindAllResponse>> getMembers(MemberQueryRequest queryRequest, Pageable pageable) {
        Page<MemberFindAllResponse> response = memberService.findAll(queryRequest, pageable);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> withdrawMember(@PathVariable Long memberId) {
        memberService.withdrawMember(memberId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pending")
    public ResponseEntity<Page<MemberPendingFindAllResponse>> getPendingMembers(Pageable pageable) {
        Page<MemberPendingFindAllResponse> response = memberService.findAllPendingMembers(pageable);
        return ResponseEntity.ok().body(response);
    }
}
