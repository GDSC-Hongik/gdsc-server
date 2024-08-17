package com.gdschongik.gdsc.infra.github.client;

import static com.gdschongik.gdsc.global.common.constant.GithubConstant.*;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.infra.github.dto.response.GithubAssignmentSubmissionResponse;
import java.io.IOException;
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
            throw new CustomException(ErrorCode.GITHUB_REPOSITORY_NOT_FOUND);
        }
    }

    public GithubAssignmentSubmissionResponse getLatestAssignmentSubmission(String repo, int week) {
        try {
            GHRepository ghRepository = getRepository(repo);
            String assignmentPath = GITHUB_ASSIGNMENT_PATH.formatted(week);

            // GHContent#getSize() 의 경우 한글 문자열을 byte 단위로 계산하기 때문에, 직접 content를 읽어서 길이를 계산
            GHContent ghContent = ghRepository.getFileContent(assignmentPath);
            String content = new String(ghContent.read().readAllBytes());

            GHCommit ghLatestCommit = ghRepository
                    .queryCommits()
                    .path(assignmentPath)
                    .list()
                    .toList()
                    .get(0);

            LocalDateTime committedAt = ghLatestCommit
                    .getCommitDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            return new GithubAssignmentSubmissionResponse(ghLatestCommit.getSHA1(), content.length(), committedAt);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.GITHUB_ASSIGNMENT_NOT_FOUND);
        }
    }
}
