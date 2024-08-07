package com.gdschongik.gdsc.infra.client.github;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubClient {

    private final GitHub github;

    public GHRepository getRepositoryLink(String ownerRepo) {
        try {
            return github.getRepository(ownerRepo);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.GITHUB_REPOSITORY_NOT_FOUND);
        }
    }
}
