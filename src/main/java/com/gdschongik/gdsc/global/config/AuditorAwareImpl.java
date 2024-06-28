package com.gdschongik.gdsc.global.config;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;

@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<Long> {

    private final MemberUtil memberUtil;

    @Override
    public Optional<Long> getCurrentAuditor() {
        try {
            return Optional.of(memberUtil.getCurrentMemberId());
        } catch (CustomException e) {
            return Optional.empty();
        }
    }
}
