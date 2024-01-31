package com.gdschongik.gdsc.domain.member.dao;

import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.config.TestQuerydslConfig;
import com.gdschongik.gdsc.global.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@Import(TestQuerydslConfig.class)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 유효하지_않은_type이면_예외_발생() {
        // given
        Pageable pageable = Pageable.ofSize(20);

        // when, then
        assertThatThrownBy(() -> memberRepository.findAll("keyword", "invalid type", pageable))
                .isInstanceOf(CustomException.class);
    }
}
