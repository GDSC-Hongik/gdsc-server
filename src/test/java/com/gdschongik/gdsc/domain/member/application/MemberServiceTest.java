package com.gdschongik.gdsc.domain.member.application;

import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.global.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    void 유효하지_않은_type이면_예외_발생() {
        // given
        Pageable pageable = Pageable.ofSize(20);

        // when, then
        assertThatThrownBy(() -> memberService.findAll("keyword", "invalid type", pageable))
                .isInstanceOf(CustomException.class);
    }
}
