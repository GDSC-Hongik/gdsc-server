package com.gdschongik.gdsc.domain.member.application;

import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.dto.request.MemberGrantRequest;
import com.gdschongik.gdsc.domain.member.dto.request.MemberUpdateRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberServiceTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Test
    void status가_DELETED라면_예외_발생() {
        // given
        Member member = Member.createGuestMember("oAuthId");
        member.withdraw();
        memberRepository.save(member);

        // when & then
        MemberUpdateRequest requestBody = new MemberUpdateRequest(
                "A111111", "name", "010-1234-5678", "department", "email@email.com", "discordUsername", "한글");
        assertThatThrownBy(() -> memberService.updateMember(member.getId(), requestBody))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MEMBER_DELETED.getMessage());
    }

    @Test
    void requirement가_충족되지_않으면_승인_실패() {
        // given
        Member unverifiedMember = Member.createGuestMember("oAuthId");
        memberRepository.save(unverifiedMember);
        MemberGrantRequest request = new MemberGrantRequest(List.of(unverifiedMember.getId()));

        // when & then
        assertThatThrownBy(() -> memberService.grantMember(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MEMBER_NOT_VERIFIED.getMessage());
    }
}
