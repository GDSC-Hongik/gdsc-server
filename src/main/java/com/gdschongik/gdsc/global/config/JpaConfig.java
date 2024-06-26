package com.gdschongik.gdsc.global.config;

import com.gdschongik.gdsc.global.security.PrincipalDetails;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return new AuditorAwareImpl();
    }

    static class AuditorAwareImpl implements AuditorAware<Long> {

        @Override
        public Optional<Long> getCurrentAuditor() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }

            PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
            return Optional.of(principal.getMemberId());
        }
    }
}
