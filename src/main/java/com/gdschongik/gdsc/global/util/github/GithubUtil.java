package com.gdschongik.gdsc.global.util.github;

import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubUtil {

    private final GitHub github;
}
