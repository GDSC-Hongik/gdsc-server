package com.gdschongik.gdsc.infra.client.github;

import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubClient {

    private final GitHub github;
}
