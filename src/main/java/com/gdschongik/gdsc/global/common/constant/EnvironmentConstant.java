package com.gdschongik.gdsc.global.common.constant;

import static com.gdschongik.gdsc.global.common.constant.EnvironmentConstant.Constants.*;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum EnvironmentConstant {
    PROD(PROD_ENV),
    DEV(DEV_ENV),
    LOCAL(LOCAL_ENV);

    private final String value;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Constants {
        public static final String PROD_ENV = "prod";
        public static final String DEV_ENV = "dev";
        public static final String LOCAL_ENV = "local";
        public static final List<String> PROD_AND_DEV_ENV = List.of(PROD_ENV, DEV_ENV);
        public static final List<String> DEV_AND_LOCAL_ENV = List.of(DEV_ENV, LOCAL_ENV);
    }
}
