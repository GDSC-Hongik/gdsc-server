package com.gdschongik.gdsc.infra.github.client;

import static com.gdschongik.gdsc.global.common.constant.GithubConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdschongik.gdsc.domain.study.domain.AssignmentSubmission;
import com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionFetchExecutor;
import com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionFetcher;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.infra.github.GithubHandleRequest;
import com.gdschongik.gdsc.infra.github.GithubHttpConnector;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.connector.GitHubConnectorResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubClient {

    private final GitHub github;
    private final GithubHttpConnector githubHttpConnector;

    public GHRepository getRepository(String ownerRepo) {
        try {
            return github.getRepository(ownerRepo);
        } catch (IOException e) {
            throw new CustomException(GITHUB_REPOSITORY_NOT_FOUND);
        }
    }

    public String getGithubHandle(String oauthId) {
        try (GitHubConnectorResponse response = githubHttpConnector.send(new GithubHandleRequest(oauthId));
                InputStream inputStream = response.bodyStream(); ) {
            // api가 login이라는 이름으로 사용자의 github handle을 반환합니다.
            return (String) new ObjectMapper().readValue(inputStream, Map.class).get("login");
        } catch (IOException e) {
            throw new CustomException(GITHUB_USER_NOT_FOUND);
        }
    }

    /**
     * 직접 요청을 수행하는 대신, fetcher를 통해 요청을 수행합니다.
     * 요청 수행 시 발생하는 예외의 경우 과제 채점에 사용되므로, 실제 요청은 채점 로직 내부에서 수행되어야 합니다.
     * 따라서 지연 평가가 가능하도록 {@link AssignmentSubmissionFetchExecutor}를 인자로 받습니다.
     * 또한, 인자로 전달된 repo와 week가 closure로 캡쳐되지 않도록 fetcher 내부에 컨텍스트로 저장합니다.
     */
    public AssignmentSubmissionFetcher getLatestAssignmentSubmissionFetcher(String repo, int week) {
        return new AssignmentSubmissionFetcher(repo, week, this::getLatestAssignmentSubmission);
    }

    private AssignmentSubmission getLatestAssignmentSubmission(String repo, int week) {
        GHRepository ghRepository = getRepository(repo);
        String assignmentPath = GITHUB_ASSIGNMENT_PATH.formatted(week);

        // GHContent#getSize() 의 경우 한글 문자열을 byte 단위로 계산하기 때문에, 직접 content를 읽어서 길이를 계산
        GHContent ghContent = getFileContent(ghRepository, assignmentPath);
        String content = readFileContent(ghContent);

        GHCommit ghLatestCommit = ghRepository
                .queryCommits()
                .path(assignmentPath)
                .list()
                .withPageSize(1)
                .iterator()
                .next();

        LocalDateTime committedAt = getCommitDate(ghLatestCommit);

        return new AssignmentSubmission(
                ghContent.getHtmlUrl(), ghLatestCommit.getSHA1(), content.length(), committedAt);
    }

    private GHContent getFileContent(GHRepository ghRepository, String filePath) {
        try {
            return ghRepository.getFileContent(filePath);
        } catch (IOException e) {
            throw new CustomException(GITHUB_CONTENT_NOT_FOUND);
        }
    }

    private String readFileContent(GHContent ghContent) {
        try (InputStream inputStream = ghContent.read()) {
            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new CustomException(GITHUB_FILE_READ_FAILED);
        }
    }

    private LocalDateTime getCommitDate(GHCommit ghLatestCommit) {
        try {
            return ghLatestCommit
                    .getCommitDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (IOException e) {
            throw new CustomException(GITHUB_COMMIT_DATE_FETCH_FAILED);
        }
    }
}
