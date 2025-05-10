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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.swagger.v3.oas.models.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
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

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            List<Tag> topPriorityTags = new ArrayList<>();
            List<Tag> bottomPriorityTags = new ArrayList<>();
            List<Tag> middlePriorityTags = new ArrayList<>();

            for (Tag tag : openApi.getTags()) {
                String tagName = tag.getName();
                if ("Member - Test".equals(tagName)) { // Test용 토큰 생성 API는 최상단 우선순위
                    topPriorityTags.add(tag);
                } else if (tagName.contains("Study") && tagName.contains("V1")) { // Study V1 API는 최하단 우선순위
                    bottomPriorityTags.add(tag);
                } else {
                    middlePriorityTags.add(tag);
                }
            }

            // 우선순위별로 태그를 합치고, 각 그룹 내에서 알파벳 순으로 정렬
            List<Tag> sortedTags = new ArrayList<>();
            topPriorityTags.sort(Comparator.comparing(Tag::getName));
            middlePriorityTags.sort(Comparator.comparing(Tag::getName));
            bottomPriorityTags.sort(Comparator.comparing(Tag::getName));

            sortedTags.addAll(topPriorityTags);
            sortedTags.addAll(middlePriorityTags);
            sortedTags.addAll(bottomPriorityTags);

            openApi.setTags(sortedTags);
        };
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
