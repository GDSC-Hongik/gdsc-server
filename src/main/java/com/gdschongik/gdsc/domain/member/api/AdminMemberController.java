package com.gdschongik.gdsc.domain.member.api;
import com.gdschongik.gdsc.domain.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {
    private final MemberService memberService;
}
