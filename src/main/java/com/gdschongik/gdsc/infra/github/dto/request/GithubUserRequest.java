package com.gdschongik.gdsc.infra.github.dto.request;

import static com.gdschongik.gdsc.global.common.constant.GithubConstant.*;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.connector.GitHubConnectorRequest;

@RequiredArgsConstructor
public class GithubUserRequest implements GitHubConnectorRequest {

    private final String oauthId;

    @Override
    public String method() {
        return "GET";
    }

    @Override
    public Map<String, List<String>> allHeaders() {
        return Map.of();
    }

    @Override
    public String header(String s) {
        return "";
    }

    @Override
    public String contentType() {
        return "";
    }

    @Override
    public InputStream body() {
        return null;
    }

    @Override
    public URL url() {
        try {
            return new URL(GITHUB_USER_API_URL.formatted(oauthId));
        } catch (MalformedURLException e) {
            throw new CustomException(ErrorCode.INTERNAL_ERROR);
        }
    }

    @Override
    public boolean hasBody() {
        return false;
    }
}
