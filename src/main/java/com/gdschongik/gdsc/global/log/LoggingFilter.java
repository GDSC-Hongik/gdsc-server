package com.gdschongik.gdsc.global.log;

import com.gdschongik.gdsc.global.common.constant.UrlConstant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Request Body를 읽어서 메모리에 저장
        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);

        // 로깅 제외 URL인 경우 로깅을 하지 않음
        String requestUri = request.getRequestURI();
        if (shouldExcludeLogging(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 요청 처리에 걸린 총 시간을 계산 (초 단위)
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(cachingRequestWrapper, response);
        long end = System.currentTimeMillis();
        double elapsedTime = (end - startTime) / 1000.0;

        // 로깅
        try {
            log.info(createHttpLogMessage(cachingRequestWrapper, response.getStatus(), elapsedTime));
        } catch (Exception e) {
            log.error("[" + this.getClass().getSimpleName() + "] Logging 실패: 알 수 없는 이유로 로깅에 실패했습니다.");
        }
    }

    private boolean shouldExcludeLogging(String requestUri) {
        return UrlConstant.getLogExcludeUrlList().contains(requestUri);
    }

    private String createHttpLogMessage(
            ContentCachingRequestWrapper requestWrapper, int httpStatusCode, double elapsedTime) {
        String httpMethod = requestWrapper.getMethod();
        String requestUri = requestWrapper.getRequestURI();
        HttpStatus httpStatus = HttpStatus.valueOf(httpStatusCode);
        String requestQuery = requestWrapper.getQueryString();
        String requestParam = new String(requestWrapper.getContentAsByteArray());

        return String.format(
                "[REQUEST] %s %s %s (%.3f) >> REQUEST_QUERY: %s >> REQUEST_PARAM: %s",
                httpMethod, requestUri, httpStatus, elapsedTime, requestQuery, requestParam);
    }
}
