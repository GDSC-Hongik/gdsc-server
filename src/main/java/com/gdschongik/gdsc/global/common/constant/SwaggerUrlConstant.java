package com.gdschongik.gdsc.global.common.constant;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SwaggerUrlConstant {
    SWAGGER_RESOURCES_URL("/swagger-resources/**"),
    SWAGGER_UI_URL("/swagger-ui/**"),
    SWAGGER_API_DOCS_URL("/v3/api-docs/**"),
    ;

    private final String value;

    public static String[] getSwaggerUrls() {
        return Arrays.stream(SwaggerUrlConstant.values())
                .map(SwaggerUrlConstant::getValue)
                .toArray(String[]::new);
    }
}
