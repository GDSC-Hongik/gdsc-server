package com.gdschongik.gdsc.global.util;

import static com.gdschongik.gdsc.global.common.constant.EnvironmentConstant.Constants.*;

import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnvironmentUtil {

    private final Environment environment;

    public String getCurrentProfile() {
        return getActiveProfiles()
                .filter(profile -> profile.equals(PROD_ENV) || profile.equals(DEV_ENV))
                .findFirst()
                .orElse(LOCAL_ENV);
    }

    public boolean isProdProfile() {
        return getActiveProfiles().anyMatch(PROD_ENV::equals);
    }

    public boolean isDevProfile() {
        return getActiveProfiles().anyMatch(DEV_ENV::equals);
    }

    public boolean isProdAndDevProfile() {
        return getActiveProfiles().anyMatch(PROD_AND_DEV_ENV::contains);
    }

    private Stream<String> getActiveProfiles() {
        return Stream.of(environment.getActiveProfiles());
    }
}
