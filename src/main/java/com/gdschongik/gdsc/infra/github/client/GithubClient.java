package com.gdschongik.gdsc.infra.github.client;

import static com.gdschongik.gdsc.global.common.constant.GithubConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.study.domain.AssignmentSubmission;
import com.gdschongik.gdsc.domain.study.domain.vo.AssignmentSubmissionFetcher;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubClient {

    private final GitHub github;

    public GHRepository getRepository(String ownerRepo) {
        try {
            return github.getRepository(ownerRepo);
        } catch (IOException e) {
            throw new CustomException(GITHUB_REPOSITORY_NOT_FOUND);
        }
    }

    public AssignmentSubmissionFetcher getLatestAssignmentSubmission(String repo, int week) {
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

        return () -> new AssignmentSubmission(
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
