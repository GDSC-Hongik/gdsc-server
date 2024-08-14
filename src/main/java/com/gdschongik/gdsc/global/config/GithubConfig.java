package com.gdschongik.gdsc.global.config;

import com.gdschongik.gdsc.global.property.GithubProperty;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GithubConfig {

    private final GithubProperty githubProperty;

    @Bean
    public GitHub github() throws IOException {
        return new GitHubBuilder().withOAuthToken(githubProperty.getSecretKey()).build();
    }
}
