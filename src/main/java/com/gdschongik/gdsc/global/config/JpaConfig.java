package com.gdschongik.gdsc.global.config;

import com.gdschongik.gdsc.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class JpaConfig {

    private final MemberUtil memberUtil;

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return new AuditorAwareImpl(memberUtil);
    }
}
