package com.gdschongik.gdsc.global.config;

import static com.gdschongik.gdsc.global.common.constant.EnvironmentConstant.Constants.*;
import static com.gdschongik.gdsc.global.common.constant.UrlConstant.*;
import static org.springframework.http.HttpHeaders.*;

import com.gdschongik.gdsc.global.util.EnvironmentUtil;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final EnvironmentUtil environmentUtil;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .servers(swaggerServers())
                .addSecurityItem(securityRequirement())
                .components(authSetting());
    }

    private List<Server> swaggerServers() {
        Server server = new Server().url(getServerUrl());
        return List.of(server);
    }

    private String getServerUrl() {
        return switch (environmentUtil.getCurrentProfile()) {
            case PROD_ENV -> PROD_SERVER_URL;
            case DEV_ENV -> DEV_SERVER_URL;
            default -> LOCAL_SERVER_URL;
        };
    }

    private Components authSetting() {
        return new Components()
                .addSecuritySchemes(
                        "Authorization",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name(AUTHORIZATION));
    }

    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement().addList(AUTHORIZATION);
    }
}
