package com.gdschongik.gdsc.global.config;

import com.gdschongik.gdsc.global.property.DockerProperty;
import io.sentry.Sentry;
import io.sentry.SentryOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SentryConfig {

    private final DockerProperty dockerProperty;

    @Bean
    Sentry.OptionsConfiguration<SentryOptions> customOptionsConfiguration() {
        return options -> {
            options.setRelease(convertTagToRelease(dockerProperty.getTag()));
        };
    }

    // gdscrepo/gdsc-server:v1.0.0 -> gdsc-server@1.0.0
    // gdscrepo/gdsc-server:ffffff -> gdsc-server@ffffff
    private String convertTagToRelease(String tag) {
        if (tag.isBlank()) {
            return "gdsc-server";
        }

        String imageWithVersion = tag.split("/")[1]; // gdsc-server:v1.0.0
        String[] split = imageWithVersion.split(":"); // [gdsc-server, v1.0.0]
        String version = split[1]; // v1.0.0 or ffffff (commit hash)
        if (version.startsWith("v")) {
            version = version.substring(1); // 1.0.0
        }
        return split[0] + "@" + version; // gdsc-server@1.0.0
    }
}
