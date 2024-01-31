package com.gdschongik.gdsc.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            log.error("JWTException : {}", e.getMessage(), e);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.setStatus(e.getErrorCode().getStatus().value());
            response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.of(e.getErrorCode())));
        }
    }
}
