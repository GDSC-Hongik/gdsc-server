package com.gdschongik.gdsc.global.util;

import static com.gdschongik.gdsc.global.common.constant.EnvironmentConstant.*;

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
                .filter(profile -> profile.equals(PROD) || profile.equals(DEV))
                .findFirst()
                .orElse(LOCAL);
    }

    public Boolean isProdProfile() {
        return getActiveProfiles().anyMatch(PROD::equals);
    }

    public Boolean isDevProfile() {
        return getActiveProfiles().anyMatch(DEV::equals);
    }

    public Boolean isProdAndDevProfile() {
        return getActiveProfiles().anyMatch(PROD_AND_DEV::contains);
    }

    private Stream<String> getActiveProfiles() {
        return Stream.of(environment.getActiveProfiles());
    }
}
