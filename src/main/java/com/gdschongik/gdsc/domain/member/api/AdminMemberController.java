package com.gdschongik.gdsc.domain.member.api;

import com.gdschongik.gdsc.domain.member.application.MemberService;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryRequest;
import com.gdschongik.gdsc.domain.member.dto.response.AdminMemberFindAllResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<Page<AdminMemberFindAllResponse>> getMembers(
            @RequestParam(value = "student-id", required = false) String studentId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "discord-username", required = false) String discordUsername,
            @RequestParam(value = "discord-nickname", required = false) String discordNickname,
            Pageable pageable) {

        MemberQueryRequest queryRequest =
                MemberQueryRequest.of(studentId, name, phone, department, email, discordUsername, discordNickname);

        Page<AdminMemberFindAllResponse> response = memberService.findAll(queryRequest, pageable);
        return ResponseEntity.ok().body(response);
    }
}
