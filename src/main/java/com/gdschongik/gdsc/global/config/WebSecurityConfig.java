package com.gdschongik.gdsc.global.config;

import static com.gdschongik.gdsc.global.common.constant.EnvironmentConstant.*;
import static com.gdschongik.gdsc.global.common.constant.UrlConstant.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.security.config.Customizer.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdschongik.gdsc.domain.auth.application.JwtService;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.global.security.CustomSuccessHandler;
import com.gdschongik.gdsc.global.security.CustomUserService;
import com.gdschongik.gdsc.global.security.JwtExceptionFilter;
import com.gdschongik.gdsc.global.security.JwtFilter;
import com.gdschongik.gdsc.global.util.CookieUtil;
import com.gdschongik.gdsc.global.util.EnviromentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final CookieUtil cookieUtil;
    private final ObjectMapper objectMapper;
    private final EnviromentUtil enviromentUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.oauth2Login(
                oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userService(customUserService(memberRepository)))
                        .successHandler(customSuccessHandler(jwtService, cookieUtil)));

        http.addFilterBefore(jwtFilter(jwtService, cookieUtil), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtExceptionFilter(objectMapper), JwtFilter.class);

        return http.build();
    }

    @Bean
    public CustomUserService customUserService(MemberRepository memberRepository) {
        return new CustomUserService(memberRepository);
    }

    @Bean
    public CustomSuccessHandler customSuccessHandler(JwtService jwtService, CookieUtil cookieUtil) {
        return new CustomSuccessHandler(jwtService, cookieUtil);
    }

    @Bean
    public JwtFilter jwtFilter(JwtService jwtService, CookieUtil cookieUtil) {
        return new JwtFilter(jwtService, cookieUtil);
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter(ObjectMapper objectMapper) {
        return new JwtExceptionFilter(objectMapper);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        if (enviromentUtil.isProdProfile()) {
            configuration.addAllowedOriginPattern(PROD_CLIENT_URL);
        }

        if (enviromentUtil.isDevProfile()) {
            configuration.addAllowedOriginPattern(DEV_CLIENT_URL);
            configuration.addAllowedOriginPattern(LOCAL_REACT_CLIENT_URL);
            configuration.addAllowedOriginPattern(LOCAL_VITE_CLIENT_URL);
        }

        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader(SET_COOKIE);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
