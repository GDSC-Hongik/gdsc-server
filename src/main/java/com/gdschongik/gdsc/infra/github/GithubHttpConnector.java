package com.gdschongik.gdsc.infra.github;

import java.io.IOException;
import org.kohsuke.github.connector.GitHubConnector;
import org.kohsuke.github.connector.GitHubConnectorRequest;
import org.kohsuke.github.connector.GitHubConnectorResponse;
import org.springframework.stereotype.Component;

@Component
public class GithubHttpConnector implements GitHubConnector {
    @Override
    public GitHubConnectorResponse send(GitHubConnectorRequest gitHubConnectorRequest) throws IOException {
        return DEFAULT.send(gitHubConnectorRequest);
    }
}
